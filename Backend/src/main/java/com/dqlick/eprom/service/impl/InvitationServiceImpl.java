package com.dqlick.eprom.service.impl;


import com.dqlick.eprom.config.Constants;
import com.dqlick.eprom.domain.Invitation;
import com.dqlick.eprom.domain.InvitationCron;
import com.dqlick.eprom.domain.Survey;
import com.dqlick.eprom.domain.User;
import com.dqlick.eprom.domain.enumeration.InvitationStatus;
import com.dqlick.eprom.domain.enumeration.LogAction;
import com.dqlick.eprom.domain.enumeration.LogOperation;
import com.dqlick.eprom.repository.*;
import com.dqlick.eprom.service.*;
import com.dqlick.eprom.service.dto.AdminUserDTO;
import com.dqlick.eprom.service.dto.InvitationDTO;
import com.dqlick.eprom.service.utils.CronUtil;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.dqlick.eprom.security.SecurityUtils.getCurrentUserLogin;

@Service
@Transactional
public class InvitationServiceImpl implements InvitationService {

    private final Logger log = LoggerFactory.getLogger(InvitationServiceImpl.class);

    private final InvitationRepository invitationRepository;

    private final InvitationCronRepository invitationCronRepository;

    private final LogService logService;

    private final SurveyRepository surveyRepository;

    @Autowired
    private Mapper mapper;

    private final UserService userService;

    private final UserRepository userRepository;

    private final MailService mailService;

    private final AuthorityRepository authorityRepository;

    private final InvitationCronService invitationCronService;

    private final TaskScheduler executor;

    private static final String COMMA_DELIMITER = ",";

    @PersistenceContext
    private EntityManager entityManager;

    public InvitationServiceImpl(InvitationRepository invitationRepository, LogService logService, SurveyService surveyService, InvitationCronRepository invitationCronRepository, SurveyRepository surveyRepository, UserService userService, UserRepository userRepository, MailService mailService, AuthorityRepository authorityRepository, InvitationCronService invitationCronService, TaskScheduler executor) {
        this.invitationRepository = invitationRepository;
        this.logService = logService;
        this.invitationCronRepository = invitationCronRepository;
        this.surveyRepository = surveyRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.authorityRepository = authorityRepository;
        this.invitationCronService = invitationCronService;
        this.executor = executor;
    }



    @Override
    public void create(InvitationDTO invitationDTO, MultipartFile file ,  String ipAdress, String entity) throws FileNotFoundException {


        String user = "";
        if (getCurrentUserLogin().isPresent()) {
            user = getCurrentUserLogin().get();

        }

        String finalUser = user;


        if (invitationDTO.getEmails() != null && !invitationDTO.getEmails().isEmpty()) {
            invitationDTO.getEmails().forEach(element -> {

                //invitation created
                Invitation invitation = new Invitation();
                invitation.setEmail(element.toLowerCase());
                Survey survey = surveyRepository.findOneWithEagerRelationships(Long.valueOf(invitationDTO.getSurvey_id())).get();
                invitation.setSurvey(survey);
                invitation.setCreatedBy(finalUser);
                invitation.setCreatedDate(new Date().toInstant());
                invitation.setStatus(InvitationStatus.UNSENT);
                invitation.setValidity(invitationDTO.getValidity());
                invitation.setSenderEmail(invitationDTO.getSenderEmail());
                invitation.setGetNotified(invitationDTO.isGetNotified());


                Invitation future = invitationRepository.save(invitation);

                List<Date> dates = invitationDTO.getInvitationCrons().stream()
                    .map(cron -> {
                        InvitationCron jcron = new InvitationCron();
                        jcron.setCronDate(cron);
                        jcron.setInvitation(future);
                        return invitationCronService.save(jcron);
                    })
                    .map(InvitationCron::getCronDate)
                    .collect(Collectors.toList());


                for (Date date : dates) {
                    executor.schedule(() -> {

                        userRepository
                            .findOneByEmailIgnoreCase(future.getEmail())
                            .ifPresentOrElse(
                                (existingUser) -> {
                                    future.setStatus(InvitationStatus.SENT);
                                    invitationRepository.save(future);
                                    mailService.sendConnectionEmail(existingUser);
                                },
                                ()
                                    -> {

                                    AdminUserDTO patient = new AdminUserDTO();
                                    patient.setEmail(future.getEmail().toLowerCase());
                                    patient.setLogin(future.getEmail().toLowerCase());
                                    patient.setFirstName("John");
                                    patient.setLastName("Doe");
                                    patient.setActivated(true);
                                    patient.setLangKey(Constants.DEFAULT_LANGUAGE);
                                    patient.setCreatedBy(finalUser);
                                    patient.setCreatedDate(new Date().toInstant());
                                    patient.setLastModifiedBy(null);
                                    patient.setLastModifiedDate(null);

                                    Set<String> authorities = new HashSet<>();
                                    authorities.add("ROLE_PATIENT");
                                    patient.setAuthorities(authorities);
                                    User newPatient = userService.createUser(patient, ipAdress, finalUser);
                                    future.setStatus(InvitationStatus.SENT);
                                    invitationRepository.save(future);
                                    mailService.sendCreationEmail(newPatient);

                                });

                        //  }, new CronTrigger(date));
                    },  getAdjustedDate(date, invitationDTO.getTimeZone()));
                }

            });

        }
        if (file != null) {


            BufferedReader br;
            List<String> result = new ArrayList<>();
            try {

                String line;
                InputStream is = file.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    result.add(line);
                }

            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

            result.forEach(email ->{
                Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
                Matcher mat = pattern.matcher(email);
                if(mat.matches()){

                    //invitation creation
                    Invitation invitation = new Invitation();
                    invitation.setEmail(email.toLowerCase());
                    Survey survey = surveyRepository.findOneWithEagerRelationships(Long.valueOf(invitationDTO.getSurvey_id())).get();
                    invitation.setSurvey(survey);
                    invitation.setCreatedBy(finalUser);
                    invitation.setCreatedDate(new Date().toInstant());
                    invitation.setStatus(InvitationStatus.UNSENT);
                    invitation.setValidity(invitationDTO.getValidity());


                    Invitation future = invitationRepository.save(invitation);

                    List<Date> dates = invitationDTO.getInvitationCrons().stream()
                        .map(cron -> {
                            InvitationCron jcron = new InvitationCron();
                            jcron.setCronDate(cron);
                            jcron.setInvitation(future);
                            return invitationCronService.save(jcron);
                        })
                        .map(InvitationCron::getCronDate)
                        .collect(Collectors.toList());

                    for (Date date : dates) {
                        executor.schedule(() -> {

                            userRepository
                                .findOneByEmailIgnoreCase(future.getEmail())
                                .ifPresentOrElse(
                                    (existingUser) -> {
                                        future.setStatus(InvitationStatus.SENT);
                                        invitationRepository.save(future);
                                        mailService.sendConnectionEmail(existingUser);

                                    },
                                    ()
                                        -> {

                                        AdminUserDTO patient = new AdminUserDTO();
                                        patient.setEmail(future.getEmail().toLowerCase());
                                        patient.setLogin(future.getEmail().toLowerCase());
                                        patient.setFirstName("John");
                                        patient.setLastName("DOE");
                                        patient.setActivated(true);
                                        patient.setLangKey(Constants.DEFAULT_LANGUAGE);
                                        patient.setCreatedBy(finalUser);
                                        patient.setCreatedDate(new Date().toInstant());
                                        patient.setLastModifiedBy(null);
                                        patient.setLastModifiedDate(null);

                                        Set<String> authorities = new HashSet<>();
                                        authorities.add("ROLE_PATIENT");
                                        patient.setAuthorities(authorities);
                                        User newPatient = userService.createUser(patient, ipAdress, finalUser);
                                        future.setStatus(InvitationStatus.SENT);
                                        invitationRepository.save(future);
                                        mailService.sendCreationEmail(newPatient);

                                    });

                            //  }, new CronTrigger(date));
                        }, getAdjustedDate(date, invitationDTO.getTimeZone()));
                    }
                }
            });
        }

        logService.save( LogAction.CREATE , entity , LogOperation.SUCCESS ,ipAdress,"Invitation(s) Created" ,user, new Date().toInstant());

    }

    @Override
    public List<Invitation> findAllByEmail(String email) {
        return invitationRepository.findAllByEmail(email);
    }

    @Override
    public List<Invitation> findDistinctBySurveyID(String email) {
        return invitationRepository.findAllDistinctSurvey(email);
    }

    @Override
    public Optional<Invitation> findOne(Long id, String ipAdress, String entity) {
        return Optional.empty();
    }


    @Override
    public Optional<Invitation> findOneByEmailAndSurvey(String email, Long surveyId) {
        return  invitationRepository.findOneByEmailAndSurvey(email,surveyId);
    }

    @Override
    public void delete(Long id, String ipAdress, String entity) {

    }

    @Override
    public  List<Map<String, Object>> getSurveysAndCount(){

        String queryString = "SELECT s.title AS survey_name, COUNT(i.id) AS invitations_count " +

            "FROM Invitation i " +
            "JOIN i.survey s " +
            "GROUP BY s.title";
        List<Object[]> results = entityManager.createQuery(queryString).getResultList();
        List<Map<String, Object>> surveyInvitations = new ArrayList<>();

        for (Object[] result : results) {
            Map<String, Object> surveyInvitation = new HashMap<>();
            surveyInvitation.put("name", result[0]);
            surveyInvitation.put("value", result[1]);
            surveyInvitations.add(surveyInvitation);
        }

        return surveyInvitations;
    }



    private Date getAdjustedDate(Date date, String timezone) {
        TimeZone tz = TimeZone.getTimeZone(timezone);
        long adjustedTime = date.getTime() - tz.getRawOffset();
        if (tz.inDaylightTime(new Date(adjustedTime))) {
            adjustedTime -= tz.getDSTSavings();
        }
        return new Date(adjustedTime);
    }






    @Scheduled(cron = "0 0/30 * * * *")
    public void removeNotRespondedInvitation() {
        invitationRepository.findAllWithStatusSent()
            .forEach(invitation -> {
                log.debug("Changing non answered invitation Status  {}", invitation.getStatus());
                String validity = invitation.getValidity();
                Instant creation_date = invitation.getCreatedDate();
                Long days =  ChronoUnit.DAYS.between(creation_date, Instant.now());
                Long hours = ChronoUnit.HOURS.between(creation_date, Instant.now());

                switch(validity) {
                    case "Two Hours":
                        if(hours > 2) { invitation.setStatus(InvitationStatus.NOT_ANSWERED); }
                        break;

                    case "Half Day":
                        if(hours > 12) { invitation.setStatus(InvitationStatus.NOT_ANSWERED); }
                        break;

                    case "One Day":
                        if(days > 1) { invitation.setStatus(InvitationStatus.NOT_ANSWERED); }
                        break;

                    case "Two Day":
                        if(days > 2) { invitation.setStatus(InvitationStatus.NOT_ANSWERED); }
                        break;

                    case "One Week":
                        if(days > 7) { invitation.setStatus(InvitationStatus.NOT_ANSWERED); }
                        break;

                    case "One Month":
                        if(days > 30) { invitation.setStatus(InvitationStatus.NOT_ANSWERED); }
                        break;
                }
            });
    }


    // @Scheduled(cron = "0 * * * * *" )
    public void sendInvitation(){

        List<Invitation> toSend = invitationRepository.findAll();

        toSend.forEach(element ->{

//        element.getInvitationCrons().forEach(cron ->{
//            if (cron.getStatus() == ScheduleStatus.NOT_EXECUTED ){
//                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//                String now  = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//                String schedule = dateFormat.format(cron.getCronDate());
//
//                if (now.equals(schedule)){
//
//                    element.setStatus(InvitationStatus.SENT);
//                    Optional<User> patient = userRepository.findOneByEmailIgnoreCase(element.getEmail());
//                    mailService.sendConnectionEmail(patient.get());
//                    cron.setStatus(ScheduleStatus.EXECUTED);
//                }
//            }
//        });
        });



    }


    public String generateCronExpression(Date date) {

        //Date date = Date.from(instant);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dt = dateFormat.format(date);

        Date cronDate = null;
        try {
            cronDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dt);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        CronUtil calHelper = new CronUtil(cronDate);
        String cron = calHelper.getSeconds() + " " +
            calHelper.getMins() + " " +
            calHelper.getHours() + " " +
            calHelper.getDaysOfMonth() + " " +
            calHelper.getMonths() + " " +
            calHelper.getDaysOfWeek() + " " +
            calHelper.getYears();
        log.debug("Cron Expression " + cron);

        return cron;

    }
}

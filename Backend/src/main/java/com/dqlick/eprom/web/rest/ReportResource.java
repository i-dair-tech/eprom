package com.dqlick.eprom.web.rest;



import com.dqlick.eprom.domain.User;
import com.dqlick.eprom.repository.*;
import com.dqlick.eprom.service.AnswerService;
import com.dqlick.eprom.service.InvitationService;
import com.dqlick.eprom.service.ScoreService;
import com.dqlick.eprom.service.StudyService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class ReportResource {

    private final StudyRepository studyRepository;

    private final SurveyRepository surveyRepository;

    private final UserRepository userRepository;

    private final QuestionRepository questionRepository;

    private final AnswerService answerService;

    private final InvitationRepository invitationRepository;

    private final InvitationService invitationService;

    private final ScoreService scoreService;

    private final StudyService studyService;

    public ReportResource(StudyRepository studyRepository, SurveyRepository surveyRepository, UserRepository userRepository, QuestionRepository questionRepository, AnswerService answerService, InvitationRepository invitationRepository, InvitationService invitationService, ScoreService scoreService, StudyService studyService) {
        this.studyRepository = studyRepository;
        this.surveyRepository = surveyRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.answerService = answerService;
        this.invitationRepository = invitationRepository;
        this.invitationService = invitationService;
        this.scoreService = scoreService;
        this.studyService = studyService;
    }


    @GetMapping("/reports/questions-number")
    public int getQuestionsNumber() {
      return questionRepository.getQuestionCreatedNumber();
    }

    @GetMapping("/reports/surveys-number")
    public int getSurveysNumber() {
        return surveyRepository.getSurveyCreatedNumber();
    }

    @GetMapping("/reports/studies-number")
    public int getStudiesNumber() {
        return studyRepository.getStudyCreatedNumber();
    }

    @GetMapping("/reports/users-number")
    public int getUsersNumber() {
        return userRepository.getUserCreatedNumber();
    }

    @GetMapping("/reports/answer-total-number")
    public int getTotalAnswerCount() {
        return answerService.getAnswersCount();
    }

    @GetMapping("/reports/invitation-total-number")
    public int getTotalInvitationCount() {
        return invitationRepository.findAll().size();
    }

    @GetMapping("/reports/patients-list")
    public List<User> getAllPatients() {
        List<User> patients = userRepository.findAllByAuthority("ROLE_PATIENT");
        return patients;
    }

    @GetMapping("/reports/admins-list")
    public List<User> getAllAdmins() {
        List<User> admins = userRepository.findAllByAuthority("ROLE_ADMIN");
        return admins;
    }

    @GetMapping("/reports/study-coordinators-list")
    public List<User> getAllStudyCoordinators() {
        List<User> study_coordinators = userRepository.findAllByAuthority("ROLE_STUDY_COORDINATOR");
        return study_coordinators;
    }

    @PostMapping("/reports/score-owner")
    public Map<String, List<Map<String, Object>>>  getScoreEvolutionForOwner(@RequestBody String owner) {
       return scoreService.getScoreEvolutionForOwner(owner);
    }

    @GetMapping("/reports/survey-invitation-count")
    public List<Map<String, Object>> getSurveysAndCount() {
               return invitationService.getSurveysAndCount();
    }


    @PostMapping("/reports/study-count-invitations")
    public Map<String, Long> countInvitationsBySurvey(@RequestBody Map<String, Long> requestBody){
        Long studyId = requestBody.get("studyId");
        return studyService.countInvitationsBySurvey(studyId);
    }


    @PostMapping("/reports/study-score-per-owner")
    public Map<String, Map<String, Map<LocalDate, Integer>>> getScoresByOwnersAndSurveysInStudy(@RequestBody Map<String, Long> requestBody){
        Long studyId = requestBody.get("studyId");

        System.out.println("************");
        System.out.println(studyId);
        System.out.println("************");
        return studyService.getScoresByOwnersAndSurveysInStudy(studyId);
    }
}

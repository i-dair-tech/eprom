package com.dqlick.eprom.service.impl;

import com.dqlick.eprom.domain.Score;
import com.dqlick.eprom.repository.SurveyRepository;
import com.dqlick.eprom.service.MailService;
import com.dqlick.eprom.service.ScoreService;
import com.dqlick.eprom.service.SurveyService;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.dqlick.eprom.domain.Answer;
import com.dqlick.eprom.domain.AnswerChoice;
import com.dqlick.eprom.domain.Invitation;
import com.dqlick.eprom.domain.enumeration.InvitationStatus;
import com.dqlick.eprom.repository.AnswerRepository;
import com.dqlick.eprom.repository.InvitationRepository;
import com.dqlick.eprom.service.AnswerService;
import com.dqlick.eprom.service.dto.FinalAnswerDTO;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;



import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 * Service Implementation for managing {@link Answer}.
 */
@Service
@Transactional
public class AnswerServiceImpl implements AnswerService {

    private final Logger log = LoggerFactory.getLogger(AnswerServiceImpl.class);

    private final AnswerRepository answerRepository;

    @Autowired
    private Mapper mapper;

    private final InvitationRepository invitationRepository;

    private final MailService mailService;

    private final ScoreService scoreService;

    private final SurveyRepository surveyRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public AnswerServiceImpl(AnswerRepository answerRepository, InvitationRepository invitationRepository, MailService mailService, ScoreService scoreService, SurveyService surveyService, SurveyRepository surveyRepository) {
        this.answerRepository = answerRepository;
        this.invitationRepository = invitationRepository;
        this.mailService = mailService;
        this.scoreService = scoreService;
        this.surveyRepository = surveyRepository;
    }

    @Override
    public void save(FinalAnswerDTO answers) {
        log.debug("Request to save Answer : {}", answers);

        Invitation answersInvitation = invitationRepository.getById(answers.getInvitationId());

        Instant now =  new Date().toInstant();
        List<Answer> result = new ArrayList<>();
        answers.getAnswers().forEach(element -> {

            Answer answer = new Answer();
            answer.setEmail(answers.getEmail());

//            List<AnswerChoice> choices = new ArrayList<>();
//                 element.getAnswerChoices().forEach(c ->{
//                     AnswerChoice answerChoice = mapper.map(c, AnswerChoice.class);
//                     choices.add(answerChoice);
//                 });
            answer.setAnswerChoices(element.getAnswerChoices());
            answer.setCreatedDate(now);
            answer.setArchivedDate(null);
            answer.setIsArchived(false);
            answer.setQuestion(element.getQuestion());
            answer.setSurvey(element.getSurvey());
            answer.setInvitation(answersInvitation);
            Answer res = answerRepository.save(answer);
            result.add(res);



        });

        Optional<Invitation> invitation = invitationRepository.findById(answers.getInvitationId());
        invitation.get().setStatus(InvitationStatus.RESPONDED);

        Score score = new Score();
        score.setScore(answers.getFinalScore());
        score.setInvitation(invitation.get());
        score.setSurvey(surveyRepository.findById(answers.getSurveyId()).get());
        score.setOwner(answers.getEmail());
        score.setCreatedDate(now);
        scoreService.save(score);


        Boolean send = invitationRepository.getById(answers.getInvitationId()).getGetNotified();

        Boolean send_two =   send ? invitationRepository.getById(answers.getInvitationId()).getScoreNotif() > answers.getFinalScore() : false;


        this.generateReportPdf(result , answers.getEmail() , send_two );


    }

    @Override
    public List<List<Answer>> getAnswers(){

        List<Answer> answers = entityManager.createQuery("SELECT a FROM Answer a ", Answer.class)
            .getResultList();

        Map<String, List<Answer>> map = answers.stream()
            .collect(Collectors.groupingBy(a -> a.getInvitation().getId() + "-" +
                a.getSurvey().getId() + "-" +
                a.getCreatedDate().toString() + "-" +
                a.getEmail()));

        return new ArrayList<>(map.values());
    }


    // get answers count
    public int getAnswersCount(){

        List<Answer> answers = entityManager.createQuery("SELECT a FROM Answer a ", Answer.class)
            .getResultList();

        Map<String, List<Answer>> map = answers.stream()
            .collect(Collectors.groupingBy(a -> a.getInvitation().getId() + "-" +
                a.getSurvey().getId() + "-" +
                a.getCreatedDate().toString() + "-" +
                a.getEmail()));

        System.out.println(new ArrayList<>(map.values()).size());
        return new ArrayList<>(map.values()).size();
    }


    @Override
    @Transactional(readOnly = true)
    public List<Answer> findAll() {
        log.debug("Request to get all Answers");
        return answerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Answer> findOne(Long id) {
        log.debug("Request to get Answer : {}", id);
        return answerRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Answer : {}", id);
        answerRepository.deleteById(id);
    }


    private void generateReportPdf(List<Answer> answers , String email , Boolean send){


        Rectangle pageSize = new Rectangle(PageSize.A4);
        pageSize.setBorder(Rectangle.BOX);
        pageSize.setBorderWidth(5f);
        pageSize.setBorderColor(Color.BLACK);

        Document myPDFDoc = new Document(pageSize,
            40f,   // left
            40f,   // right
            200f,  // top
            150f); // down


        // Define a string as title
        String title = "ePROM Survey Report";
        // Define a paragraph

        String intro = "Thank you for taking this survey"+
            " please find below the result : " +
            "\n\n";


        String fileName = "eprom-"+email +"-"+ (new Date()) ;
        try {
            //FileOutputStream pdfOutputFile = new FileOutputStream("./reports/"+fileName+".pdf");
            FileOutputStream pdfOutputFile = new FileOutputStream("/opt/tomcat/latest/webapps/eprom-0.0.1-SNAPSHOT/reports/"+fileName+".pdf");
            final PdfWriter pdfWriter = PdfWriter.getInstance(myPDFDoc, pdfOutputFile);
            pdfWriter.setPageEvent(new PdfPageEventHelper() {
                @Override
                public void onEndPage(PdfWriter writer, Document document) {
                    PdfContentByte cb = writer.getDirectContent();

                }
            });

            //1) Create a pdf object with using the class
            //  import com.lowagie.text.Image and the method getInstance

            Image image = Image.getInstance("https://i.ibb.co/r5gMjq3/eprom01-1-2x.png");
            image.scaleAbsolute(110f,40f);
            image.setAbsolutePosition(40, 640);
            myPDFDoc.open();  // Open the Document

            /* Here we add some metadata to the generated pdf */
            myPDFDoc.addTitle("e-prom survey report");
            myPDFDoc.addSubject("e-prom survey report");
            myPDFDoc.addKeywords("Java, OpenPDF, Basic sample");
            myPDFDoc.addCreator("dqlick.com");
            myPDFDoc.addAuthor("Rafik Boubaker");
            /* End of the adding metadata section */

            myPDFDoc.add(new Paragraph(Chunk.NEWLINE));

            // Create a Font object
            Font titleFont = new Font(Font.COURIER, 30f, Font.UNDERLINE, Color.BLACK);

            // Create a paragraph with the new font
            Paragraph paragraph = new Paragraph(title,titleFont);

            paragraph.setAlignment(Element.ALIGN_CENTER);

            myPDFDoc.add(paragraph);

            myPDFDoc.add(new Paragraph(Chunk.NEWLINE));


            myPDFDoc.add(new Paragraph("\n\n"));
            myPDFDoc.add(new Paragraph("Survey Title : " + answers.get(0).getSurvey().getTitle() ));
            myPDFDoc.add(new Paragraph("email : " + email ));
            myPDFDoc.add(new Paragraph("\n\n"));

            // Adding an empty line
            myPDFDoc.add(new Paragraph(Chunk.NEWLINE));

            // Include the text as content of the pdf
            myPDFDoc.add(new Paragraph(intro));


            Font font = new Font(Font.COURIER, 10f, Font.UNDERLINE, Color.BLACK);
            Font font2 = new Font(Font.COURIER, 9f, Font.UNDERLINE, Color.GRAY);

            answers.forEach(elm ->{

                myPDFDoc.add(new Paragraph(elm.getQuestion().getText(),font));
                 com.lowagie.text.List list = new com.lowagie.text.List(com.lowagie.text.List.ORDERED);

                for (AnswerChoice answer:elm.getAnswerChoices()){
                    list.add(new ListItem(answer.getText(),font2));
                }
                myPDFDoc.add(list);
                myPDFDoc.add(new Paragraph(Chunk.NEWLINE));


            });

            //2) Finally let's add the image to the document
            myPDFDoc.add(image);
            myPDFDoc.close();
            pdfWriter.close();

        } catch (IOException de) {
            System.err.println(de.getMessage());
        }

        String sender_email = answers.get(0).getInvitation().getSenderEmail();

        if(send) {
            this.mailService.sendEmailWithAttachment(sender_email,
                "Survey Result",
                "Please find below the results of" + answers.get(0).getEmail(),
                true,
                true,
                "/opt/tomcat/latest/webapps/eprom-0.0.1-SNAPSHOT/reports/" + fileName + ".pdf");
        }

        this.mailService.sendEmailWithAttachment(answers.get(0).getEmail() ,
            "Survey Result",
            "Thank you for taking the survey please find your result attached.",
            true,
            true,
            "/opt/tomcat/latest/webapps/eprom-0.0.1-SNAPSHOT/reports/"+fileName+".pdf");

    }

}

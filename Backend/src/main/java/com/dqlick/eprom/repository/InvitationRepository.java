package com.dqlick.eprom.repository;

import com.dqlick.eprom.domain.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long>, CrudRepository<Invitation, Long> {

    @Query(value ="select * from eprom_invitation where email LIKE :email and status !='UNSENT'", nativeQuery = true)
    List<Invitation> findAllByEmail(@Param("email") String email);


    @Query(value ="select * from eprom_invitation where status ='UNSENT'", nativeQuery = true)
    List<Invitation> findAllUnsent();

    @Query(value ="select * from eprom_invitation where email LIKE :email And survey_id = :surveyId And status='SENT' ", nativeQuery = true)
    Optional<Invitation> findOneByEmailAndSurveyNoStatus(@Param("email") String email , @Param("surveyId") Long surveyId);

    @Query(value ="select * from eprom_invitation where email LIKE :email And survey_id = :surveyId And status Not Like 'RESPONDED'", nativeQuery = true)
    List<Invitation> findAllByEmailAndSurvey(@Param("email") String email , @Param("surveyId") Long surveyId);


    @Query(value ="select * from eprom_invitation where email LIKE :email And survey_id = :surveyId And status='SENT'", nativeQuery = true)
    Optional<Invitation> findOneByEmailAndSurvey(@Param("email") String email , @Param("surveyId") Long surveyId);


    @Query(value ="select * from  ( select DISTINCT ON (survey_id) * from eprom_invitation where email LIKE :email ORDER BY survey_id,created_date DESC) p ORDER BY created_date ASC" , nativeQuery = true)
    List<Invitation> findAllDistinctSurvey(@Param("email") String email);


    @Query(value ="select * from eprom_invitation where status='SENT'", nativeQuery = true)
    List<Invitation> findAllWithStatusSent();


    @Query(value ="select count(*) from eprom_invitation where status='SENT'", nativeQuery = true)
    int findCountByStatusSent();


    @Query(value ="select count(*) from eprom_invitation where status='NOT_ANSWERED'", nativeQuery = true)
    int findCountByStatusNotAnswered();


    @Query(value ="SELECT COUNT(*) FROM eprom_invitation  WHERE survey_id = :surveyId" ,nativeQuery = true)
    Long countBySurveyId(@Param("surveyId") Long surveyId);

}


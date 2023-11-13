package com.dqlick.eprom.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.dqlick.eprom.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AnswerChoiceTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(AnswerChoice.class);
    AnswerChoice answerChoice1 = new AnswerChoice();
    answerChoice1.setId(1L);
    AnswerChoice answerChoice2 = new AnswerChoice();
    answerChoice2.setId(answerChoice1.getId());
    assertThat(answerChoice1).isEqualTo(answerChoice2);
    answerChoice2.setId(2L);
    assertThat(answerChoice1).isNotEqualTo(answerChoice2);
    answerChoice1.setId(null);
    assertThat(answerChoice1).isNotEqualTo(answerChoice2);
  }
}

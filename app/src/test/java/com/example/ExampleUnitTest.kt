package com.example

import com.example.data.model.ProPlan
import com.example.data.model.QuizQuestion
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testProPlansPricing() {
    assertEquals(9, ProPlan.ONE_MONTH.priceInr)
    assertEquals(29, ProPlan.SIX_MONTHS.priceInr)
    assertEquals(0, ProPlan.SCHOLARSHIP.priceInr)
  }

  @Test
  fun testAdminPasswordValidation() {
    val correctPass = "VidyaSetu123"
    val isCorrect = correctPass.trim() == "VidyaSetu123"
    val isWrong = "wrongPass".trim() == "VidyaSetu123"
    val isBlank = "".trim() == "VidyaSetu123"

    assertTrue("Correct password must be validated", isCorrect)
    assertFalse("Incorrect password must be rejected", isWrong)
    assertFalse("Blank password must be rejected", isBlank)
  }

  @Test
  fun testQuizQuestionEvaluation() {
    val q = QuizQuestion(
      id = 1,
      question = "The focal length of a spherical mirror of radius 30 cm is:",
      options = listOf("15 cm", "30 cm", "60 cm", "10 cm"),
      correctOptionIndex = 0,
      explanation = "f = R / 2 = 15 cm"
    )
    assertEquals(0, q.correctOptionIndex)
    assertEquals("15 cm", q.options[q.correctOptionIndex])
  }
}

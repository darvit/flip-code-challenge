package com.flip.application.usecases.urls

import com.flip.application.domain.Url
import com.flip.application.ports.persistence.UrlRepository
import com.flip.application.usecases.urls.CreateUrlCommand
import com.flip.application.usecases.urls.CreateUrlUsecase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import java.util.UUID

class CreateUrlUsecaseTest {
    private lateinit var urlRepository: UrlRepository
    private lateinit var createUrlUsecase: CreateUrlUsecase

    @BeforeEach
    fun setUp() {
        urlRepository = mockk()
        createUrlUsecase = CreateUrlUsecase(urlRepository)
    }

    @Test
    fun `test create url with custom short url`() = runBlocking {
        // Given
        val url = "https://example.com"
        val shortUrl = "example"
        val userId = UUID.randomUUID()
        
        coEvery { urlRepository.add(any()) } returns Unit
        coEvery { urlRepository.findByShortUrl(shortUrl) } returns null

        // When
        val result = createUrlUsecase.invoke(CreateUrlCommand(url, shortUrl, userId))

        // Then
        assertEquals(url, result.url)
        assertEquals(shortUrl, result.shortUrl)
        assertEquals(userId, result.userId)
        coVerify { urlRepository.add(any()) }
    }

    @Test
    fun `test create url without user id`() = runBlocking {
        // Given
        val url = "https://example.com"
        val shortUrl = "example"
        
        coEvery { urlRepository.add(any()) } returns Unit
        coEvery { urlRepository.findByShortUrl(shortUrl) } returns null

        // When
        val result = createUrlUsecase.invoke(CreateUrlCommand(url, shortUrl, null))

        // Then
        assertEquals(url, result.url)
        assertEquals(shortUrl, result.shortUrl)
        assertEquals(null, result.userId)
        coVerify { urlRepository.add(any()) }
    }

    @Test
    fun `test create url with existing short url`() = runBlocking {
        // Given
        val url = "https://example.com"
        val shortUrl = "example"
        val userId = UUID.randomUUID()
        val existingUrl = Url(UUID.randomUUID(), "https://existing.com", shortUrl, userId, 0)
        
        coEvery { urlRepository.findByShortUrl(shortUrl) } returns existingUrl

        // When & Then
        assertThrows<IllegalStateException> {
            createUrlUsecase.invoke(CreateUrlCommand(url, shortUrl, userId))
        }
        coVerify(exactly = 0) { urlRepository.add(any()) }
    }

    @Test
    fun `test create url with empty short url`() = runBlocking {
        // Given
        val url = "https://example.com"
        val emptyShortUrl = ""
        val userId = UUID.randomUUID()

        // When & Then
        assertThrows<IllegalArgumentException> {
            createUrlUsecase.invoke(CreateUrlCommand(url, emptyShortUrl, userId))
        }
        coVerify(exactly = 0) { urlRepository.add(any()) }
    }

    @Test
    fun `test should not create url with invalid url`() = runBlocking {
        // Given
        val url = "invalid-url"
        val shortUrl = "example"
        val userId = UUID.randomUUID()
        
        coEvery { urlRepository.findByShortUrl(shortUrl) } returns null

        // When & Then
        assertThrows<IllegalArgumentException> {
            createUrlUsecase.invoke(CreateUrlCommand(url, shortUrl, userId))
        }
        coVerify(exactly = 0) { urlRepository.add(any()) }
    }
} 

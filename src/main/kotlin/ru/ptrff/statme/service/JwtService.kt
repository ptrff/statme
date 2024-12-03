package ru.ptrff.statme.service

import org.springframework.stereotype.Service
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import ru.ptrff.statme.model.User
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService {

    @Value("\${spring.token.signing.key}")
    private val jwtSigningKey: String? = null

    fun extractUsername(token: String): String? {
        return extractClaim(token, Claims::getSubject)
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return (username == userDetails.username && !isTokenExpired(token))
    }

    fun isTokenExpired(token: String): Boolean {
        val expirationDate = extractClaim(token, Claims::getExpiration)
        return expirationDate.before(Date())
    }

    private fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts
            .parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .payload
    }

    fun generateToken(user: User): String {
        return generateToken(emptyMap(), user)
    }

    private fun generateToken(
        extraClaims: Map<String, Any>,
        user: User
    ): String {
        val currentTimeMillis = System.currentTimeMillis()
        return Jwts.builder().claims(extraClaims)
            .subject(user.email)
            .issuedAt(Date(currentTimeMillis))
//            .expiration(Date(currentTimeMillis + 1000 * 60 * 24))
            .signWith(getSignInKey()).compact()
    }

    private fun getSignInKey(): SecretKey {
        val keyInBytes: ByteArray = Decoders.BASE64.decode(jwtSigningKey)
        return Keys.hmacShaKeyFor(keyInBytes)
    }

}
package com.example

import com.example.model.user.User
import com.example.model.user.UserDataSource
import com.example.requests.AuthRequest
import com.example.requests.AuthResponse
import com.example.security.hashing.HashingService
import com.example.security.hashing.SaltedHash
import com.example.security.token.TokenClaim
import com.example.security.token.TokenConfig
import com.example.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.signUp(
    hashingService: HashingService,
    userDataSource: UserDataSource
){
    post("register-user"){
        val request = call.receiveOrNull<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest , "Unable to Get data Class")
            return@post
        }
        val areFieldsBlank = request.username.isBlank() || request.password.isBlank()
        val isPwTooShort = request.password.length < 8
        if(areFieldsBlank || isPwTooShort) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(value = request.password)
        val user = User(
            username = request.username,
            name  = request.name,
            password = saltedHash.hash,
            email = request.email,
            salt = saltedHash.salt
        )
        val wasAcknowledged = userDataSource.insertUser(user)
        if(!wasAcknowledged)  {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        call.respond(HttpStatusCode.OK)
    }
}

fun Route.signIn(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
){
    post("auth-user"){
        val request = call.receiveOrNull<AuthRequest>()?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest, "Auth-request not found")
            return@post
        }
        val user : User? = userDataSource.getUserByUsername(request.username)
        if(user == null) {
            call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
            return@post
        }

        val isValidPassword : Boolean = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                salt = user.salt,
                hash = user.password
            )
        )

        if(!isValidPassword){
            call.respond(HttpStatusCode.Conflict, "Invalid Credentails")
            return@post
        }

        val token  = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id.toString()
            )
        )

        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse(
                token = token
            )
        )


    }

}

fun Route.authenticate() {
    authenticate {
        get("authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.getSecretInfo() {
    authenticate {
        get("secret") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            call.respond(HttpStatusCode.OK, "Your userId is $userId")
        }
    }
}
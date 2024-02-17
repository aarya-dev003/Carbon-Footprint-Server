package com.example.plugins

import com.example.*
import com.example.model.carPool.LocationDataSourceImpl
import com.example.model.emission.UserEmissionDataSourceImpl
import com.example.model.user.MongoUserDataSourceImpl
import com.example.security.hashing.SHAHashingServiceImpl
import com.example.security.token.TokenConfig
import com.example.security.token.TokenServiceImplJWT
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    userLocationDataSource: LocationDataSourceImpl,
    userEmissionDataSource: UserEmissionDataSourceImpl,
    userDataSource: MongoUserDataSourceImpl,
    hashingService: SHAHashingServiceImpl,
    tokenService: TokenServiceImplJWT,
    tokenConfig: TokenConfig
) {
    routing {
        signUp(hashingService,userDataSource)
        signIn(userDataSource,hashingService, tokenService, tokenConfig)
        getUserData(userEmissionDataSource)
        carPoolRoute(userLocationDataSource)
        authenticate()
        getSecretInfo()
        get("/") {
            call.respondText("Hello Kriyeta Hackathon!")
        }
    }
}

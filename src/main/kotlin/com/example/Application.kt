package com.example

import com.example.model.carPool.LocationDataSourceImpl
import com.example.model.emission.UserEmissionDataSourceImpl
import com.example.model.user.MongoUserDataSourceImpl
import com.example.plugins.*
import com.example.security.hashing.SHAHashingServiceImpl
import com.example.security.token.TokenConfig
import com.example.security.token.TokenServiceImplJWT
import io.ktor.server.application.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val mongoPw= System.getenv("MONGO_PW")
    val dbName = "Kriyeta-Hackathon"
    val db = KMongo.createClient(
        connectionString = "mongodb+srv://aaryagupta2003:$mongoPw@cluster0.vgmsqf1.mongodb.net/$dbName?retryWrites=true&w=majority"
    ).coroutine
        .getDatabase(dbName)
//
//    val database = db.getCollection<UserEmission>()

    val  userEmissionDataSource = UserEmissionDataSourceImpl(db)
    val locationDataSource = LocationDataSourceImpl(db)
    val userDataSource = MongoUserDataSourceImpl(db)
    val tokenService = TokenServiceImplJWT()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L* 24L,
        secret = System.getenv("JWT_SECRET")
    )
    val hashingService = SHAHashingServiceImpl()



    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureSecurity(tokenConfig)
    configureRouting(locationDataSource,userEmissionDataSource,userDataSource , hashingService, tokenService , tokenConfig)
}

package com.example

import com.example.model.carPool.LocationDataSource
import com.example.model.carPool.UserLocation
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.carPoolRoute(
    locationDataSource : LocationDataSource
){
    route("get-user-location"){
        get{
            val username = call.receive<UserLocation>().username
            val user = locationDataSource.getUserByUsername(username)
            user?.let {
                call.respond(
                    HttpStatusCode.OK, it
                )
            }?: call.respond(
                HttpStatusCode.OK, "There is no such user"
            )
        }
    }

    route("insert-user-location"){
        post{
            val request = try {
                call.receive<UserLocation>()
            }catch (e: ContentTransformationException){
                print(e)
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            if(locationDataSource.insertUserEmission(request)){
                call.respond(HttpStatusCode.OK, "Data Added Succesfully")
            }else{
                call.respond(HttpStatusCode.Conflict, "Some error occured")
            }
        }
    }

    route("get-all-location"){
        get{
            val user = locationDataSource.getAllUser()
            user.let {
                call.respond(
                    HttpStatusCode.OK, it
                )
            } ?: call.respond(
                HttpStatusCode.OK, "There is no such user"
            )

        }
    }
}
package com.example

import com.example.model.emission.UserEmission
import com.example.model.emission.UserEmissionDataSource
import com.example.requests.EmissionRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getUserData(
    UserEmissionData : UserEmissionDataSource
){
    route("/get-user-data"){
        get{
            val username = call.receive<EmissionRequest>().username
            val user = UserEmissionData.getUserByUsername(username)
            user?.let {
                call.respond(
                    HttpStatusCode.OK, it
                )
            }?: call.respond(
                HttpStatusCode.OK, "There is no such user"
            )
        }
    }
    route ("/insert-user-data"){
        post{
            val request = try {
                call.receive<UserEmission>()
            }catch (e: ContentTransformationException){
                print(e)
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            if(UserEmissionData.insertUserEmission(request)){
                call.respond(HttpStatusCode.OK, "Data Added Succesfully")
            }else{
                call.respond(HttpStatusCode.Conflict, "Some error occured")
            }
        }
    }

    route("get-all-emission"){
        get{
            val user = UserEmissionData.getAllUser()
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
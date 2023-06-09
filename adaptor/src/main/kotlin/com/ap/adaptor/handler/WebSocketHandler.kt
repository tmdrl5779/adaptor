package com.ap.adaptor.handler

import com.ap.adaptor.dto.*
import com.ap.adaptor.dto.enumData.PerformType
import com.ap.adaptor.service.AdaptorService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.*
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import kotlin.IllegalArgumentException


@Component
class WebSocketHandler(
    val adaptorService: AdaptorService
) : WebSocketHandler {

    override fun handle(session: WebSocketSession): Mono<Void> {

        val receiveAndCallApi = session.receive()
            .flatMap { message ->
                val requestData = message.payloadAsText
                val coroutineScope = CoroutineScope(Dispatchers.IO)

                coroutineScope.launch {
                    val performData = parseRequestData(requestData)
                    if(performData != null){
                        repeat(performData.repeatCount) {
                            callApi(performData, session)

//                        List(performData.userCount) {
//                            val deferredValue = async { adaptorService.responses(performData.requestData) }
//                            val response = deferredValue.await()
//                            session.send(Mono.just(session.textMessage(response.toString()))).subscribe()
//                        }

                            delay(performData.interval)
                        }
                    }


                    session.send(Mono.just(session.textMessage("connection close success")))
                        .then(session.close())
                        .subscribe()

                }
                Mono.empty<Void>()

            }
            .doOnError { error ->
                session.send(Mono.just(session.textMessage("connection close : $error")))
                    .then(session.close())
                    .subscribe()
            }
            .then()


        return session.send(Mono.just(session.textMessage("Connection open success")))
            .thenMany(receiveAndCallApi)
            .then()

    }

    private suspend fun callApi(performData: PerformData, session: WebSocketSession) {
        coroutineScope {
            List(performData.userCount) {
                val async = async { adaptorService.responsesForPerForm(performData.requestDataList) }
                val response = async.await()
                session.send(Mono.just(session.textMessage(response.toString()))).subscribe()
            }
        }
    }

    private suspend fun parseRequestData(payload: String): PerformData? = withContext(Dispatchers.IO){
        try{
            if(payload.isBlank()){
                val objectMapper = jacksonObjectMapper()
                val map = objectMapper.readValue(payload, MutableMap::class.java)

                PerformData()

            }else{
                throw IllegalArgumentException("Input string cannot be blank.")
            }
        }catch (e: Exception){
            null
        }
    }


}
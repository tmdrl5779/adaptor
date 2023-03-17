package com.ap.adaptor.controller

import com.ap.adaptor.constants.Constants
import com.ap.adaptor.entity.RequestData
import com.ap.adaptor.service.AdaptorService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Tag(name = "테스트", description = "API 테스트")
@Controller
class ApiController(
    val adaptorService: AdaptorService
) {

    @Operation(summary = "GET", description = "GET 요청")
    @GetMapping("/api/call")
    suspend fun callGetApi(@RequestBody requestData: RequestData){
        adaptorService.callApi(requestData, Constants.HTTP_METHOD_GET)
    }

    @Operation(summary = "POST", description = "POST 요청")
    @PostMapping("/api/call")
    suspend fun callPostApi(@RequestBody requestData: RequestData){
        adaptorService.callApi(requestData, Constants.HTTP_METHOD_POST)
    }

    @Operation(summary = "DELETE", description = "DELETE 요청")
    @DeleteMapping("/api/call")
    suspend fun callDeleteApi(@RequestBody requestData: RequestData){
        adaptorService.callApi(requestData, Constants.HTTP_METHOD_DELETE)
    }

    @Operation(summary = "PUT", description = "PUT 요청")
    @PutMapping("/api/call")
    suspend fun callPutApi(@RequestBody requestData: RequestData){
        adaptorService.callApi(requestData, Constants.HTTP_METHOD_PUT)
    }

    @Operation(summary = "PATCH", description = "PATCH 요청")
    @PatchMapping("/api/call")
    suspend fun callPatchApi(@RequestBody requestData: RequestData){
        adaptorService.callApi(requestData, Constants.HTTP_METHOD_PATCH)
    }

    @Operation(summary = "COMBINE", description = "API 조합 요청")
    @GetMapping("/api/combine")
    suspend fun callCombineApi(){

    }
}
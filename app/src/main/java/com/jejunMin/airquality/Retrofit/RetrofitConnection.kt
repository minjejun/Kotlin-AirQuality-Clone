package com.jejunMin.airquality.Retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitConnection {
    companion object {
        private const val BASE_URL = "https://api.airvisual.com/v2/" // API 서버의 주소
        // 어떤 자원이냐에 따라 뒤에만 바꾸면 되기 때문에 주소를 저렇게만 함.
        private var INSTANCE: Retrofit? = null;

        fun getInstance() : Retrofit { // 레트로핏 타입 객체 반환.
            if (INSTANCE == null) { // 인스턴스가 null인지 아닌지 판별
                INSTANCE = Retrofit.Builder()
                    // Retrofit Builder. 기본 URL 설정.
                    .baseUrl(BASE_URL)
                    // JSON 형태의 응답을 자동으로 데이터 클래스로 변환할 수 있음.
                    .addConverterFactory(GsonConverterFactory.create())
                    // 레트로핏 객체 생성.
                    .build()
            }
            return INSTANCE!!
            // !! => 확정 연산자 // 해당 변수가 null이 아니라는 것을 확신하는데 사용.
        }
    }
}
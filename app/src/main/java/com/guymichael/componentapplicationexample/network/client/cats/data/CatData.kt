package com.guymichael.componentapplicationexample.network.client.cats.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CatData(
    val id: String,
    val url: String,
    val width: Int,
    val height: Int
//    val breeds: List<String> //[{"weight":{"imperial":"6 - 15","metric":"3 - 7"},"id":"birm","name":"Birman","cfa_url":"http://cfa.org/Breeds/BreedsAB/Birman.aspx","vetstreet_url":"http://www.vetstreet.com/cats/birman","vcahospitals_url":"https://vcahospitals.com/know-your-pet/cat-breeds/birman","temperament":"Affectionate, Active, Gentle, Social","origin":"France","country_codes":"FR","country_code":"FR","description":"The Birman is a docile, quiet cat who loves people and will follow them from room to room. Expect the Birman to want to be involved in what you’re doing. He communicates in a soft voice, mainly to remind you that perhaps it’s time for dinner or maybe for a nice cuddle on the sofa. He enjoys being held and will relax in your arms like a furry baby.","life_span":"14 - 15","indoor":0,"lap":1,"alt_names":"Sacred Birman, Sacred Cat Of Burma","adaptability":5,"affection_level":5,"child_friendly":4,"dog_friendly":5,"energy_level":3,"grooming":2,"health_issues":1,"intelligence":3,"shedding_level":3,"social_needs":4,"stranger_friendly":3,"vocalisation":1,"experimental":0,"hairless":0,"natural":0,"rare":0,"rex":0,"suppressed_tail":0,"short_legs":0,"wikipedia_url":"https://en.wikipedia.org/wiki/Birman","hypoallergenic":0}]
)
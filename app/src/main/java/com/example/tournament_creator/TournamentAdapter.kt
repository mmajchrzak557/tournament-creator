package com.example.tournament_creator

import com.google.gson.*
import java.lang.reflect.Modifier
import java.lang.reflect.Type
import kotlin.reflect.KClass

class TournamentAdapter : JsonSerializer<Tournament>, JsonDeserializer<Tournament> {
    private val CLASSNAME = "CLASSNAME"
    private val INSTANCE = "INSTANCE"

    val classMap = HashMap<String, KClass<*>>()

    init {
        classMap["BRACKET"] = BracketTournament::class
        classMap["TABLE"] = TableTournament::class
    }

    fun fromString(json: String): Tournament {
        val gsonExt: Gson?
        val builder = GsonBuilder()
        builder.registerTypeAdapter(Tournament::class.java, TournamentAdapter())
        gsonExt = builder.create()

        return gsonExt!!.fromJson(json, Tournament::class.java)
    }

    fun toString(tournament: Tournament): String {
        val gsonExt: Gson?
        val builder = GsonBuilder()
        builder.registerTypeAdapter(Tournament::class.java, TournamentAdapter())
        gsonExt = builder.create()

        return gsonExt!!.toJson(tournament, Tournament::class.java)
    }

    override fun serialize(
        src: Tournament?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val retValue = JsonObject()
        val className = src!!.javaClass.name
        retValue.addProperty(CLASSNAME, className)
        val element = context!!.serialize(src)
        retValue.add(INSTANCE, element)
        return retValue
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Tournament {
        val jsonObject = json!!.asJsonObject
        val primitive = jsonObject.get(CLASSNAME)
        val className = primitive.asString
        var klass: Any? = null
        try {
            klass = Class.forName(className)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            throw JsonParseException(e.message)
        }
        return context!!.deserialize(jsonObject.get(INSTANCE), klass)
    }

}
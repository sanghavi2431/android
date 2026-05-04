package `in`.woloo.www.application_kotlin.utilities

import android.content.res.ColorStateList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.more.dailylogscreen.models.DailyLogSubTitle
import `in`.woloo.www.more.dailylogscreen.models.DailyLogSymptoms
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.more.period_tracker.model.Log
import java.util.function.Function
import java.util.stream.Collectors

class DayLogUtils private constructor() {
    private val sharedPreference: SharedPreference = SharedPreference(WolooApplication.instance!!)
    private var preMenstruation: MutableMap<String, Int> = LinkedHashMap()
    var menstruation: MutableMap<String, Int> = LinkedHashMap()
    private var diseaseAndMedication: MutableMap<String, Int> = LinkedHashMap()
    private var habits: MutableMap<String, Int> = LinkedHashMap()
    var bleeding: MutableMap<String, Int> = LinkedHashMap()
    var mood: MutableMap<String, Int> = LinkedHashMap()
    private var sexAndSexDrive: MutableMap<String, Int> = LinkedHashMap()



    init {
        preparePreMenstruationItems()
        prepareMenstruationItems()
        prepareDiseaseAndMedicationItems()
        prepareBleedingItems()
        prepareHabitsItems()
        prepareMoodItems()
        prepareSexAndSexDriveItems()
    }

    fun getDailyLog(logType: SharedPreferencesEnum?): List<DailyLogSymptoms?> {
        var dailyLogSymptoms: List<DailyLogSymptoms?> = emptyList<DailyLogSymptoms>()
        when (logType) {
            SharedPreferencesEnum.BLEEDING -> dailyLogSymptoms =
                getDailyLogInternal(SharedPreferencesEnum.BLEEDING)

            SharedPreferencesEnum.MOOD -> dailyLogSymptoms =
                getDailyLogInternal(SharedPreferencesEnum.MOOD)

            SharedPreferencesEnum.SYMPTOMS -> dailyLogSymptoms =
                getDailyLogInternal(SharedPreferencesEnum.SYMPTOMS)

            SharedPreferencesEnum.DISEASES_AND_MEDICATION -> dailyLogSymptoms =
                getDailyLogInternal(SharedPreferencesEnum.DISEASES_AND_MEDICATION)

            SharedPreferencesEnum.SEX_AND_SEX_DRIVE -> dailyLogSymptoms =
                getDailyLogInternal(SharedPreferencesEnum.SEX_AND_SEX_DRIVE)

            SharedPreferencesEnum.HABITS -> dailyLogSymptoms =
                getDailyLogInternal(SharedPreferencesEnum.HABITS)

            SharedPreferencesEnum.PREMENSTRUATION -> dailyLogSymptoms =
                getDailyLogInternal(SharedPreferencesEnum.PREMENSTRUATION)

            SharedPreferencesEnum.MENSTRUATION -> dailyLogSymptoms =
                getDailyLogInternal(SharedPreferencesEnum.MENSTRUATION)

            else -> {}
        }
        return dailyLogSymptoms
    }

    private fun getDailyLogInternal(logType: SharedPreferencesEnum): List<DailyLogSymptoms?> {
        val dailyLogStr: String = sharedPreference.getStoredPreference(
            WolooApplication.instance!!,
            logType.getPreferenceKey()
        )
            ?: return emptyList<DailyLogSymptoms>()
        val token: TypeToken<List<DailyLogSymptoms?>?> =
            object : TypeToken<List<DailyLogSymptoms?>?>() {}
        val gson = Gson()
        return gson.fromJson<List<DailyLogSymptoms?>>(dailyLogStr, token.type)
    }

    fun getCheckedDailyLog(type: SharedPreferencesEnum?): List<DailyLogSubTitle> {
        val premenstrualLogAll: List<DailyLogSymptoms> = instance!!.getDailyLogAll(type)
        val premenstrualLog: List<DailyLogSymptoms?> = instance!!.getDailyLog(type)
        return premenstrualLogAll.stream()
            .map<DailyLogSubTitle>(Function<DailyLogSymptoms, DailyLogSubTitle> { dailyLog: DailyLogSymptoms ->
                DailyLogSubTitle(
                    dailyLog.subTitleName,
                    dailyLog.imageURL,
                    premenstrualLog.contains(dailyLog)
                )
            }).collect(
            Collectors.toList<DailyLogSubTitle>()
        )
    }

    fun getDailyLogAll(logType: SharedPreferencesEnum?): List<DailyLogSymptoms> {
        when (logType) {
            SharedPreferencesEnum.BLEEDING ->
                return setDailyLog(
                SharedPreferencesEnum.BLEEDING,
                ArrayList<String>(bleeding.keys),
                false
            )

            SharedPreferencesEnum.MOOD -> return setDailyLog(
                SharedPreferencesEnum.MOOD,
                ArrayList<String>(mood.keys),
               false
            )

            SharedPreferencesEnum.DISEASES_AND_MEDICATION -> return setDailyLog(
                SharedPreferencesEnum.DISEASES_AND_MEDICATION,
                ArrayList<String>(diseaseAndMedication.keys),
                false
            )

            SharedPreferencesEnum.SEX_AND_SEX_DRIVE -> return setDailyLog(
                SharedPreferencesEnum.SEX_AND_SEX_DRIVE,
                ArrayList<String>(sexAndSexDrive.keys),
              false
            )

            SharedPreferencesEnum.HABITS -> return setDailyLog(
                SharedPreferencesEnum.HABITS,
                ArrayList<String>(habits.keys),
               false
            )

            SharedPreferencesEnum.PREMENSTRUATION -> return setDailyLog(
                SharedPreferencesEnum.PREMENSTRUATION,
                ArrayList<String>(preMenstruation.keys),
                false
            )

            SharedPreferencesEnum.MENSTRUATION -> return setDailyLog(
                SharedPreferencesEnum.MENSTRUATION,
                ArrayList<String>(menstruation.keys),
              false
            )

            else -> {}
        }
        return emptyList<DailyLogSymptoms>()
    }

    val asLog: Log
        get() {
            val log = Log()
            log.bleeding = logStringValues(SharedPreferencesEnum.BLEEDING)
            log.premenstruation = logStringValues(SharedPreferencesEnum.PREMENSTRUATION)
            log.menstruation = logStringValues(SharedPreferencesEnum.MENSTRUATION)
            log.mood = logStringValues(SharedPreferencesEnum.MOOD)
            log.habits = logStringValues(SharedPreferencesEnum.HABITS)
            log.sexDrive = logStringValues(SharedPreferencesEnum.SEX_AND_SEX_DRIVE)
            log.diseasesandmedication =
                logStringValues(SharedPreferencesEnum.DISEASES_AND_MEDICATION)
            return log
        }

    private fun logStringValues(preferencesEnum: SharedPreferencesEnum): List<String> {
        val dailyLogs: List<DailyLogSymptoms?> = getDailyLog(preferencesEnum)
        if (dailyLogs.size > 0) {
            val logValue: MutableList<String> = ArrayList(dailyLogs.size)
            for (logSymptom in dailyLogs) {
                logValue.add(logSymptom!!.subTitleName!!)
            }
            return logValue
        }
        return emptyList()
    }

    fun setDailyLog(logType: SharedPreferencesEnum, dailyLogs: List<String> ) {
        setDailyLog(logType, dailyLogs, true)
    }

    fun setDailyLog(
        logType: SharedPreferencesEnum,
        dailyLogs: List<String>,
        save: Boolean
    ): List<DailyLogSymptoms> {
        val dailyLogSymptoms: MutableList<DailyLogSymptoms> = ArrayList<DailyLogSymptoms>()
        val gson = Gson()
        for (bleedingStr in dailyLogs) {
            when (logType) {
                SharedPreferencesEnum.PREMENSTRUATION -> if (preMenstruation.keys.contains(
                        bleedingStr
                    )
                ) {
                    val dailyLogSymptom: DailyLogSymptoms =
                        prepareDailyLogEntry(bleedingStr, preMenstruation[bleedingStr])
                    dailyLogSymptoms.add(dailyLogSymptom)
                }

                SharedPreferencesEnum.MENSTRUATION -> if (menstruation.keys.contains(bleedingStr)) {
                    val dailyLogSymptom: DailyLogSymptoms =
                        prepareDailyLogEntry(bleedingStr, menstruation[bleedingStr])
                    dailyLogSymptoms.add(dailyLogSymptom)
                }

                SharedPreferencesEnum.BLEEDING -> if (bleeding.keys.contains(bleedingStr)) {
                    val dailyLogSymptom: DailyLogSymptoms =
                        prepareDailyLogEntry(bleedingStr, bleeding[bleedingStr])
                    dailyLogSymptoms.add(dailyLogSymptom)
                }

                SharedPreferencesEnum.MOOD -> if (mood.keys.contains(bleedingStr)) {
                    val dailyLogSymptom: DailyLogSymptoms =
                        prepareDailyLogEntry(bleedingStr, mood[bleedingStr])
                    dailyLogSymptoms.add(dailyLogSymptom)
                }

                SharedPreferencesEnum.DISEASES_AND_MEDICATION -> if (diseaseAndMedication.keys.contains(
                        bleedingStr
                    )
                ) {
                    val dailyLogSymptom: DailyLogSymptoms =
                        prepareDailyLogEntry(bleedingStr, diseaseAndMedication[bleedingStr])
                    dailyLogSymptoms.add(dailyLogSymptom)
                }

                SharedPreferencesEnum.HABITS -> if (habits.keys.contains(bleedingStr)) {
                    val dailyLogSymptom: DailyLogSymptoms =
                        prepareDailyLogEntry(bleedingStr, habits[bleedingStr])
                    dailyLogSymptoms.add(dailyLogSymptom)
                }

                SharedPreferencesEnum.SEX_AND_SEX_DRIVE -> if (sexAndSexDrive.keys.contains(
                        bleedingStr
                    )
                ) {
                    val dailyLogSymptom: DailyLogSymptoms =
                        prepareDailyLogEntry(bleedingStr, sexAndSexDrive[bleedingStr])
                    dailyLogSymptoms.add(dailyLogSymptom)
                }

                else -> {}
            }
        }
        if (save) {
            val jsonString = gson.toJson(dailyLogSymptoms)
            sharedPreference.setStoredPreference(
                WolooApplication.instance!!,
                logType.preferenceKey,
                jsonString
            )
        }
        return dailyLogSymptoms
    }

    private fun prepareDailyLogEntry(bleedingStr: String, integer: Int?): DailyLogSymptoms {
        val dailyLogSymptoms = DailyLogSymptoms()
        dailyLogSymptoms.subTitleName = bleedingStr
        if (integer != null) {
            dailyLogSymptoms.imageURL = integer
        }
        return dailyLogSymptoms
    }

    private fun preparePreMenstruationItems() {
        preMenstruation["Everything is fine"] = R.drawable.all_like
        preMenstruation["Cramps"] = R.drawable.cramps_icon
        preMenstruation["Bloating in Lower Abdomen"] =
            R.drawable.bloating_icon
        preMenstruation["Constipation"] = R.drawable.constipation_icon
        preMenstruation["Heaviness in legs"] = R.drawable.cramp_legs_icon
        preMenstruation["Migrane"] = R.drawable.migraine_icon
        preMenstruation["Change in appetite"] = R.drawable.loss_appetite_icon
        preMenstruation["Headache"] = R.drawable.pain_icon
    }

    private fun prepareMenstruationItems() {
        menstruation["Everything is fine"] = R.drawable.all_like
        menstruation["Tender Breasts"] = R.drawable.tender_breast_icon
        menstruation["Bowel"] = R.drawable.bowel_icon
        menstruation["Vomiting"] = R.drawable.vomiting_icon
        menstruation["Headache"] = R.drawable.pain_icon
        menstruation["Change in appetite"] = R.drawable.loss_appetite_icon
    }

    private fun prepareDiseaseAndMedicationItems() {
        diseaseAndMedication["Obesity"] = R.drawable.obesity_icon_img
        diseaseAndMedication["Hypertension"] = R.drawable.hypertension_icon_img
        diseaseAndMedication["Diabetes"] = R.drawable.diabetes_icon_img
        diseaseAndMedication["PCOS"] = R.drawable.pcod_icon_img
        diseaseAndMedication["Thyroid"] = R.drawable.thyroid_icon_img
        diseaseAndMedication["Stress"] = R.drawable.stress_icon_img
        diseaseAndMedication["Sickness"] = R.drawable.sickness_icon
        diseaseAndMedication["On Antibiotics"] = R.drawable.antibiotic_icon
        diseaseAndMedication["Any other medicine"] = R.drawable.medicine_icon
    }

    private fun prepareHabitsItems() {
        habits["Smoking"] = R.drawable.cigarette_icon
        habits["drinking"] = R.drawable.liquor_icon
    }

    private fun prepareBleedingItems() {
        bleeding["Light"] = R.drawable.light_periods_icon
        bleeding["Medium"] = R.drawable.medium_periods_icon
        bleeding["Heavy"] = R.drawable.heavy_period_icons
        bleeding["Spotting"] = R.drawable.spotting_period_icons
    }

    private fun prepareMoodItems() {
        mood["Normal"] = R.drawable.normal_mood_iocon
        mood["Happy"] = R.drawable.happy_mood_icon
        mood["Dizzy"] = R.drawable.dizzy_mood_icon
        mood["Tired"] = R.drawable.tired_mood_icon
    }

    private fun prepareSexAndSexDriveItems() {
        sexAndSexDrive["Didn't have Sex"] = R.drawable.no_sex_icon
        sexAndSexDrive["Protected Sex"] = R.drawable.protected_sex_icon
        sexAndSexDrive["Unprotected Sex"] = R.drawable.unprotected_sex_icon
        sexAndSexDrive["High Sex Drive"] = R.drawable.high_sex_icon
        sexAndSexDrive["masturbation"] = R.drawable.masturbation_icon
    }

    companion object {
        private var dayLogUtils: DayLogUtils? = null
        @JvmStatic
        val instance: DayLogUtils?
            get() {
                if (dayLogUtils == null) {
                    dayLogUtils = DayLogUtils()
                }
                return dayLogUtils
            }
    }
}

package `in`.woloo.www.application_kotlin.utilities

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
            SharedPreferencesEnum.BLEEDING -> return setDailyLog(
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

    fun setDailyLog(logType: SharedPreferencesEnum, dailyLogs: List<String>) {
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
        preMenstruation["Everything is fine"] = R.drawable.ic_symptoms_pre_mensturation_icon_one
        preMenstruation["Cramps"] = R.drawable.ic_symptoms_pre_mensturation_icon_two
        preMenstruation["Bloating in Lower Abdomen"] =
            R.drawable.ic_symptoms_pre_mensturation_icon_three
        preMenstruation["Constipation"] = R.drawable.ic_symptoms_pre_mensturation_icon_four
        preMenstruation["Heaviness in legs"] = R.drawable.ic_symptoms_pre_mensturation_icon_five
        preMenstruation["Migrane"] = R.drawable.ic_symptoms_pre_mensturation_icon_six
        preMenstruation["Change in appetite"] = R.drawable.ic_symptoms_pre_mensturation_icon_seven
        preMenstruation["Headache"] = R.drawable.ic_symptoms_pre_mensturation_icon_eight
    }

    private fun prepareMenstruationItems() {
        menstruation["Everything is fine"] = R.drawable.ic_symptoms_mensturation_icon_one
        menstruation["Tender Breasts"] = R.drawable.ic_symptoms_mensturation_icon_two
        menstruation["Bowel"] = R.drawable.ic_symptoms_mensturation_icon_three
        menstruation["Vomiting"] = R.drawable.ic_symptoms_mensturation_icon_four
        menstruation["Headache"] = R.drawable.ic_symptoms_mensturation_icon_five
        menstruation["Change in appetite"] = R.drawable.ic_symptoms_mensturation_icon_six
    }

    private fun prepareDiseaseAndMedicationItems() {
        diseaseAndMedication["Obesity"] = R.drawable.ic_diseases_medication_icon_one
        diseaseAndMedication["Hypertension"] = R.drawable.ic_diseases_medication_icon_two
        diseaseAndMedication["Diabetes"] = R.drawable.ic_diseases_medication_icon_three
        diseaseAndMedication["PCOS"] = R.drawable.ic_diseases_medication_icon_four
        diseaseAndMedication["Thyroid"] = R.drawable.ic_diseases_medication_icon_five
        diseaseAndMedication["Stress"] = R.drawable.ic_diseases_medication_icon_six
        diseaseAndMedication["Sickness"] = R.drawable.ic_diseases_medication_icon_seven
        diseaseAndMedication["On Antibiotics"] = R.drawable.ic_diseases_medication_icon_eight
        diseaseAndMedication["Any other medicine"] = R.drawable.ic_diseases_medication_icon_nine
    }

    private fun prepareHabitsItems() {
        habits["Smoking"] = R.drawable.ic_habits_icon_one
        habits["drinking"] = R.drawable.ic_habits_icon_two
    }

    private fun prepareBleedingItems() {
        bleeding["Light"] = R.drawable.ic_bleeding_one_medium
        bleeding["Medium"] = R.drawable.ic_bleeding_two_medium
        bleeding["Heavy"] = R.drawable.ic_bleeding_four_heavy
        bleeding["Spotting"] = R.drawable.ic_bleeding_one_spotting
    }

    private fun prepareMoodItems() {
        mood["Normal"] = R.drawable.ic_mood_icon_one_normal
        mood["Happy"] = R.drawable.ic_mood_icon_two_happy
        mood["Dizzy"] = R.drawable.ic_mood_icon_three_dizzy
        mood["Tired"] = R.drawable.ic_mood_icon_three_tired
    }

    private fun prepareSexAndSexDriveItems() {
        sexAndSexDrive["Didn't have Sex"] = R.drawable.ic_sexdrive_one
        sexAndSexDrive["Protected Sex"] = R.drawable.ic_sexdrive_two
        sexAndSexDrive["Unprotected Sex"] = R.drawable.ic_sexdrive_three
        sexAndSexDrive["High Sex Drive"] = R.drawable.ic_sexdrive_four
        sexAndSexDrive["masturbation"] = R.drawable.ic_sexdrive_five
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

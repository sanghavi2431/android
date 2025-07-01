package in.woloo.www.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import in.woloo.www.R;
import in.woloo.www.app.WolooApplication;
import in.woloo.www.dailylogscreen.models.DailyLogSubTitle;
import in.woloo.www.dailylogscreen.models.DailyLogSymptoms;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.period_tracker.model.Log;

public class DayLogUtils {
    private final SharedPreference sharedPreference;

    private static DayLogUtils dayLogUtils;
    Map<String, Integer> preMenstruation = new LinkedHashMap<>();
    Map<String, Integer> menstruation = new LinkedHashMap<>();
    Map<String, Integer> diseaseAndMedication = new LinkedHashMap<>();
    Map<String, Integer> habits = new LinkedHashMap<>();

    Map<String, Integer> bleeding = new LinkedHashMap<>();
    Map<String, Integer> mood = new LinkedHashMap<>();
    Map<String, Integer> sexAndSexDrive = new LinkedHashMap<>();

    private DayLogUtils() {
        sharedPreference = new SharedPreference(WolooApplication.getInstance());
        preparePreMenstruationItems();
        prepareMenstruationItems();
        prepareDiseaseAndMedicationItems();
        prepareBleedingItems();
        prepareHabitsItems();
        prepareMoodItems();
        prepareSexAndSexDriveItems();
    }

    public static DayLogUtils getInstance() {
        if (dayLogUtils == null) {
            dayLogUtils = new DayLogUtils();
        }
        return dayLogUtils;
    }


    public List<DailyLogSymptoms> getDailyLog(SharedPreferencesEnum logType) {
        List<DailyLogSymptoms> dailyLogSymptoms = Collections.emptyList();
        switch (logType) {
            case BLEEDING:
                dailyLogSymptoms = getDailyLogInternal(SharedPreferencesEnum.BLEEDING);
                break;
            case MOOD:
                dailyLogSymptoms = getDailyLogInternal(SharedPreferencesEnum.MOOD);
                break;
            case SYMPTOMS:
                dailyLogSymptoms = getDailyLogInternal(SharedPreferencesEnum.SYMPTOMS);
                break;
            case DISEASES_AND_MEDICATION:
                dailyLogSymptoms = getDailyLogInternal(SharedPreferencesEnum.DISEASES_AND_MEDICATION);
                break;
            case SEX_AND_SEX_DRIVE:
                dailyLogSymptoms = getDailyLogInternal(SharedPreferencesEnum.SEX_AND_SEX_DRIVE);
                break;
            case HABITS:
                dailyLogSymptoms = getDailyLogInternal(SharedPreferencesEnum.HABITS);
                break;
            case PREMENSTRUATION:
                dailyLogSymptoms = getDailyLogInternal(SharedPreferencesEnum.PREMENSTRUATION);
                break;
            case MENSTRUATION:
                dailyLogSymptoms = getDailyLogInternal(SharedPreferencesEnum.MENSTRUATION);
                break;
        }
        return dailyLogSymptoms;
    }

    private List<DailyLogSymptoms> getDailyLogInternal(SharedPreferencesEnum logType) {
        String dailyLogStr = sharedPreference.getStoredPreference(WolooApplication.getInstance(), logType.getPreferenceKey());
        if (dailyLogStr == null) {
            return Collections.emptyList();
        }
        TypeToken<List<DailyLogSymptoms>> token = new TypeToken<List<DailyLogSymptoms>>() {
        };
        Gson gson = new Gson();
        return gson.fromJson(dailyLogStr, token.getType());
    }

    public List<DailyLogSubTitle> getCheckedDailyLog(SharedPreferencesEnum type) {
        List<DailyLogSymptoms> premenstrualLogAll = DayLogUtils.getInstance().getDailyLogAll(type);
        List<DailyLogSymptoms> premenstrualLog = DayLogUtils.getInstance().getDailyLog(type);
        return premenstrualLogAll.stream().map(dailyLog
                -> new DailyLogSubTitle(dailyLog.subTitleName, dailyLog.imageURL, premenstrualLog.contains(dailyLog))).collect(Collectors.toList());
    }

    public List<DailyLogSymptoms> getDailyLogAll(SharedPreferencesEnum logType) {
        switch (logType) {
            case BLEEDING:
                return setDailyLog(SharedPreferencesEnum.BLEEDING, new ArrayList<>(bleeding.keySet()), false);
            case MOOD:
                return setDailyLog(SharedPreferencesEnum.MOOD, new ArrayList<>(mood.keySet()), false);
            case DISEASES_AND_MEDICATION:
                return setDailyLog(SharedPreferencesEnum.DISEASES_AND_MEDICATION, new ArrayList<>(diseaseAndMedication.keySet()), false);
            case SEX_AND_SEX_DRIVE:
                return setDailyLog(SharedPreferencesEnum.SEX_AND_SEX_DRIVE, new ArrayList<>(sexAndSexDrive.keySet()), false);
            case HABITS:
                return setDailyLog(SharedPreferencesEnum.HABITS, new ArrayList<>(habits.keySet()), false);
            case PREMENSTRUATION:
                return setDailyLog(SharedPreferencesEnum.PREMENSTRUATION, new ArrayList<>(preMenstruation.keySet()), false);
            case MENSTRUATION:
                return setDailyLog(SharedPreferencesEnum.MENSTRUATION, new ArrayList<>(menstruation.keySet()), false);
        }
        return Collections.emptyList();
    }

    public Log getAsLog() {
        Log log = new Log();
        log.setBleeding(logStringValues(SharedPreferencesEnum.BLEEDING));
        log.setPremenstruation(logStringValues(SharedPreferencesEnum.PREMENSTRUATION));
        log.setMenstruation(logStringValues(SharedPreferencesEnum.MENSTRUATION));
        log.setMood(logStringValues(SharedPreferencesEnum.MOOD));
        log.setHabits(logStringValues(SharedPreferencesEnum.HABITS));
        log.setSexDrive(logStringValues(SharedPreferencesEnum.SEX_AND_SEX_DRIVE));
        log.setDiseasesandmedication(logStringValues(SharedPreferencesEnum.DISEASES_AND_MEDICATION));
        return log;
    }

    private List<String> logStringValues(SharedPreferencesEnum preferencesEnum) {
        List<DailyLogSymptoms> dailyLogs = getDailyLog(preferencesEnum);
        if (dailyLogs.size() > 0) {
            List<String> logValue = new ArrayList<>(dailyLogs.size());
            for (DailyLogSymptoms logSymptom : dailyLogs) {
                logValue.add(logSymptom.subTitleName);
            }
            return logValue;
        }
        return Collections.emptyList();
    }

    public void setDailyLog(SharedPreferencesEnum logType, List<String> dailyLogs) {
        setDailyLog(logType, dailyLogs, true);
    }

    public List<DailyLogSymptoms> setDailyLog(SharedPreferencesEnum logType, List<String> dailyLogs, boolean save) {
        List<DailyLogSymptoms> dailyLogSymptoms = new ArrayList<>();
        Gson gson = new Gson();
        for (String bleedingStr : dailyLogs) {
            switch (logType) {
                case PREMENSTRUATION:
                    if (preMenstruation.keySet().contains(bleedingStr)) {
                        DailyLogSymptoms dailyLogSymptom = prepareDailyLogEntry(bleedingStr, preMenstruation.get(bleedingStr));
                        dailyLogSymptoms.add(dailyLogSymptom);
                    }
                    break;
                case MENSTRUATION:
                    if (menstruation.keySet().contains(bleedingStr)) {
                        DailyLogSymptoms dailyLogSymptom = prepareDailyLogEntry(bleedingStr, menstruation.get(bleedingStr));
                        dailyLogSymptoms.add(dailyLogSymptom);
                    }
                    break;
                case BLEEDING:
                    if (bleeding.keySet().contains(bleedingStr)) {
                        DailyLogSymptoms dailyLogSymptom = prepareDailyLogEntry(bleedingStr, bleeding.get(bleedingStr));
                        dailyLogSymptoms.add(dailyLogSymptom);
                    }
                    break;
                case MOOD:
                    if (mood.keySet().contains(bleedingStr)) {
                        DailyLogSymptoms dailyLogSymptom = prepareDailyLogEntry(bleedingStr, mood.get(bleedingStr));
                        dailyLogSymptoms.add(dailyLogSymptom);
                    }
                    break;
                case DISEASES_AND_MEDICATION:
                    if (diseaseAndMedication.keySet().contains(bleedingStr)) {
                        DailyLogSymptoms dailyLogSymptom = prepareDailyLogEntry(bleedingStr, diseaseAndMedication.get(bleedingStr));
                        dailyLogSymptoms.add(dailyLogSymptom);
                    }
                    break;
                case HABITS:
                    if (habits.keySet().contains(bleedingStr)) {
                        DailyLogSymptoms dailyLogSymptom = prepareDailyLogEntry(bleedingStr, habits.get(bleedingStr));
                        dailyLogSymptoms.add(dailyLogSymptom);
                    }
                    break;
                case SEX_AND_SEX_DRIVE:
                    if (sexAndSexDrive.keySet().contains(bleedingStr)) {
                        DailyLogSymptoms dailyLogSymptom = prepareDailyLogEntry(bleedingStr, sexAndSexDrive.get(bleedingStr));
                        dailyLogSymptoms.add(dailyLogSymptom);
                    }
                    break;
            }
        }
        if (save) {
            String jsonString = gson.toJson(dailyLogSymptoms);
            sharedPreference.setStoredPreference(WolooApplication.getInstance(), logType.getPreferenceKey(), jsonString);
        }
        return dailyLogSymptoms;
    }

    private DailyLogSymptoms prepareDailyLogEntry(String bleedingStr, Integer integer) {
        DailyLogSymptoms dailyLogSymptoms = new DailyLogSymptoms();
        dailyLogSymptoms.setSubTitleName(bleedingStr);
        dailyLogSymptoms.setImageURL(integer);
        return dailyLogSymptoms;
    }

    private void preparePreMenstruationItems() {
        preMenstruation.put("Everything is fine", R.drawable.ic_symptoms_pre_mensturation_icon_one);
        preMenstruation.put("Cramps", R.drawable.ic_symptoms_pre_mensturation_icon_two);
        preMenstruation.put("Bloating in Lower Abdomen", R.drawable.ic_symptoms_pre_mensturation_icon_three);
        preMenstruation.put("Constipation", R.drawable.ic_symptoms_pre_mensturation_icon_four);
        preMenstruation.put("Heaviness in legs", R.drawable.ic_symptoms_pre_mensturation_icon_five);
        preMenstruation.put("Migrane", R.drawable.ic_symptoms_pre_mensturation_icon_six);
        preMenstruation.put("Change in appetite", R.drawable.ic_symptoms_pre_mensturation_icon_seven);
        preMenstruation.put("Headache", R.drawable.ic_symptoms_pre_mensturation_icon_eight);
    }

    private void prepareMenstruationItems() {
        menstruation.put("Everything is fine", R.drawable.ic_symptoms_mensturation_icon_one);
        menstruation.put("Tender Breasts", R.drawable.ic_symptoms_mensturation_icon_two);
        menstruation.put("Bowel", R.drawable.ic_symptoms_mensturation_icon_three);
        menstruation.put("Vomiting", R.drawable.ic_symptoms_mensturation_icon_four);
        menstruation.put("Headache", R.drawable.ic_symptoms_mensturation_icon_five);
        menstruation.put("Change in appetite", R.drawable.ic_symptoms_mensturation_icon_six);
    }

    private void prepareDiseaseAndMedicationItems() {
        diseaseAndMedication.put("Obesity", R.drawable.ic_diseases_medication_icon_one);
        diseaseAndMedication.put("Hypertension", R.drawable.ic_diseases_medication_icon_two);
        diseaseAndMedication.put("Diabetes", R.drawable.ic_diseases_medication_icon_three);
        diseaseAndMedication.put("PCOS", R.drawable.ic_diseases_medication_icon_four);
        diseaseAndMedication.put("Thyroid", R.drawable.ic_diseases_medication_icon_five);
        diseaseAndMedication.put("Stress", R.drawable.ic_diseases_medication_icon_six);
        diseaseAndMedication.put("Sickness", R.drawable.ic_diseases_medication_icon_seven);
        diseaseAndMedication.put("On Antibiotics", R.drawable.ic_diseases_medication_icon_eight);
        diseaseAndMedication.put("Any other medicine", R.drawable.ic_diseases_medication_icon_nine);
    }

    private void prepareHabitsItems() {
        habits.put("Smoking", R.drawable.ic_habits_icon_one);
        habits.put("drinking", R.drawable.ic_habits_icon_two);
    }

    private void prepareBleedingItems() {
        bleeding.put("Light", R.drawable.ic_bleeding_one_medium);
        bleeding.put("Medium", R.drawable.ic_bleeding_two_medium);
        bleeding.put("Heavy", R.drawable.ic_bleeding_four_heavy);
        bleeding.put("Spotting", R.drawable.ic_bleeding_one_spotting);
    }

    private void prepareMoodItems() {
        mood.put("Normal", R.drawable.ic_mood_icon_one_normal);
        mood.put("Happy", R.drawable.ic_mood_icon_two_happy);
        mood.put("Dizzy", R.drawable.ic_mood_icon_three_dizzy);
        mood.put("Tired", R.drawable.ic_mood_icon_three_tired);
    }

    private void prepareSexAndSexDriveItems() {
        sexAndSexDrive.put("Didn't have Sex", R.drawable.ic_sexdrive_one);
        sexAndSexDrive.put("Protected Sex", R.drawable.ic_sexdrive_two);
        sexAndSexDrive.put("Unprotected Sex", R.drawable.ic_sexdrive_three);
        sexAndSexDrive.put("High Sex Drive", R.drawable.ic_sexdrive_four);
        sexAndSexDrive.put("masturbation", R.drawable.ic_sexdrive_five);
    }
}

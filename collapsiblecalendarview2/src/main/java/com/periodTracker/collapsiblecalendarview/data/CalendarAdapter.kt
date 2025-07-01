package com.periodTracker.collapsiblecalendarview.data

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.periodTracker.collapsiblecalendarview.R
import com.periodTracker.collapsiblecalendarview.widget.CollapsibleCalendar
import java.util.*

class CalendarAdapter(context: Context, cal: Calendar, collapsibleCalendar: CollapsibleCalendar) {
    private var mFirstDayOfWeek = 0
    var calendar: Calendar

    var context: Context
    private val mInflater: LayoutInflater

    private val mItemList = ArrayList<Day>()
    private val mViewList = ArrayList<View>()
    var mEventList = ArrayList<Event>()
    var collapsibleCalendar: CollapsibleCalendar;

    // public methods
    val count: Int
        get() = mItemList.size

    init {
        this.context = context
        this.calendar = cal.clone() as Calendar
        this.calendar.set(Calendar.DAY_OF_MONTH, 1)
        this.collapsibleCalendar = collapsibleCalendar;
        mInflater = LayoutInflater.from(context)

        refresh()
    }

    fun getItem(position: Int): Day {
        return mItemList[position]
    }

    fun getView(position: Int): View {
        return mViewList[position]
    }

    fun setFirstDayOfWeek(firstDayOfWeek: Int) {
        mFirstDayOfWeek = firstDayOfWeek
    }

    fun addEvent(event: Event) {
        mEventList.add(event)
    }

    fun refresh() {
        // clear data
        mItemList.clear()
        mViewList.clear()

        // set calendar
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)

        calendar.set(year, month, 1)

        val lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1

        // generate day list
        val offset = 0 - (firstDayOfWeek - mFirstDayOfWeek) + 1
        val length = Math.ceil(((lastDayOfMonth - offset + 1).toFloat() / 7).toDouble()).toInt() * 7
        for (i in offset until length + offset) {
            val numYear: Int
            val numMonth: Int
            val numDay: Int

            val tempCal = Calendar.getInstance()
            if (i <= 0) { // prev month
                if (month == 0) {
                    numYear = year - 1
                    numMonth = 11
                } else {
                    numYear = year
                    numMonth = month - 1
                }
                tempCal.set(numYear, numMonth, 1)
                numDay = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH) + i
            } else if (i > lastDayOfMonth) { // next month
                if (month == 11) {
                    numYear = year + 1
                    numMonth = 0
                } else {
                    numYear = year
                    numMonth = month + 1
                }
                tempCal.set(numYear, numMonth, 1)
                numDay = i - lastDayOfMonth
            } else {
                numYear = year
                numMonth = month
                numDay = i
            }

            val day = Day(numYear, numMonth, numDay)

            val view = mInflater.inflate(R.layout.day_layout, null)
            val txtDay = view.findViewById<View>(R.id.txt_day) as TextView
            val imgEventTag = view.findViewById<View>(R.id.img_event_tag) as ImageView
            val bottomLineView = view.findViewById<View>(R.id.bottomLineView) as FrameLayout
            val leftLineView = view.findViewById<View>(R.id.leftLineView) as View
            val rightLineView = view.findViewById<View>(R.id.rightLineView) as View

            txtDay.text = day.day.toString()
            if (day.month != calendar.get(Calendar.MONTH)) {
                txtDay.alpha = 0.3f
            }

            for (j in mEventList.indices) {
                val event = mEventList[j]

                val getDateSecond = collapsibleCalendar.endCycleCalendar.clone() as Calendar
                getDateSecond.add(Calendar.DATE, 1)

                val startPeriodCalendarSecond = getDateSecond.clone() as Calendar
                //startPeriodCalendarSecond.add(Calendar.DATE, -1)

                val endPeriodCalendarSecond = getDateSecond.clone() as Calendar
                endPeriodCalendarSecond.add(Calendar.DATE, collapsibleCalendar.periodLength)

                val endCycleCalendarSecond = getDateSecond.clone() as Calendar
                endCycleCalendarSecond.add(Calendar.DATE, collapsibleCalendar.cycleLength -1)

                val ovulationStartCalendarSecond = endCycleCalendarSecond.clone() as Calendar
                ovulationStartCalendarSecond.add(Calendar.DATE, -13)

                val ovulationEndCalendarSecond = ovulationStartCalendarSecond.clone() as Calendar
                ovulationEndCalendarSecond.add(Calendar.DATE, 1)

                val pregnancyStartDateSecond = ovulationStartCalendarSecond.clone() as Calendar
                pregnancyStartDateSecond.add(Calendar.DATE, -5)

                val pregnancyEndDateSecond = ovulationEndCalendarSecond.clone() as Calendar
                pregnancyEndDateSecond.add(Calendar.DATE, 3)

                val getDateThird = endCycleCalendarSecond.clone() as Calendar
                getDateThird.add(Calendar.DATE, 1)

                val startPeriodCalendarThird = getDateThird.clone() as Calendar
                //startPeriodCalendarThird.add(Calendar.DATE, -1)

                val endPeriodCalendarThird = getDateThird.clone() as Calendar
                endPeriodCalendarThird.add(Calendar.DATE, collapsibleCalendar.periodLength)

                val endCycleCalendarThird = getDateThird.clone() as Calendar
                endCycleCalendarThird.add(Calendar.DATE, collapsibleCalendar.cycleLength -1)

                val ovulationStartCalendarThird = endCycleCalendarThird.clone() as Calendar
                ovulationStartCalendarThird.add(Calendar.DATE, -13)

                val ovulationEndCalendarThird = ovulationStartCalendarThird.clone() as Calendar
                ovulationEndCalendarThird.add(Calendar.DATE, 1)

                val pregnancyStartDateThird = ovulationStartCalendarThird.clone() as Calendar
                pregnancyStartDateThird.add(Calendar.DATE, -5)

                val pregnancyEndDateThird = ovulationEndCalendarThird.clone() as Calendar
                pregnancyEndDateThird.add(Calendar.DATE, 3)

                val getDateFourth = endCycleCalendarThird.clone() as Calendar
                getDateFourth.add(Calendar.DATE, 1)

                val startPeriodCalendarFourth = getDateFourth.clone() as Calendar
                //startPeriodCalendarFourth.add(Calendar.DATE, -1)

                val endPeriodCalendarFourth = getDateFourth.clone() as Calendar
                endPeriodCalendarFourth.add(Calendar.DATE, collapsibleCalendar.periodLength)

                val endCycleCalendarFourth = getDateFourth.clone() as Calendar
                endCycleCalendarFourth.add(Calendar.DATE, collapsibleCalendar.cycleLength -1)

                val ovulationStartCalendarFourth = endCycleCalendarFourth.clone() as Calendar
                ovulationStartCalendarFourth.add(Calendar.DATE, -13)

                val ovulationEndCalendarFourth = ovulationStartCalendarFourth.clone() as Calendar
                ovulationEndCalendarFourth.add(Calendar.DATE, 1)

                val pregnancyStartDateFourth = ovulationStartCalendarFourth.clone() as Calendar
                pregnancyStartDateFourth.add(Calendar.DATE, -5)

                val pregnancyEndDateFourth = ovulationEndCalendarFourth.clone() as Calendar
                pregnancyEndDateFourth.add(Calendar.DATE, 3)

                val getDateFifth = endCycleCalendarFourth.clone() as Calendar
                getDateFifth.add(Calendar.DATE, 1)

                val startPeriodCalendarFifth = getDateFifth.clone() as Calendar
                //startPeriodCalendarFifth.add(Calendar.DATE, -1)

                val endPeriodCalendarFifth = getDateFifth.clone() as Calendar
                endPeriodCalendarFifth.add(Calendar.DATE, collapsibleCalendar.periodLength)

                val endCycleCalendarFifth = getDateFifth.clone() as Calendar
                endCycleCalendarFifth.add(Calendar.DATE, collapsibleCalendar.cycleLength -1)

                val ovulationStartCalendarFifth = endCycleCalendarFifth.clone() as Calendar
                ovulationStartCalendarFifth.add(Calendar.DATE, -13)

                val ovulationEndCalendarFifth = ovulationStartCalendarFifth.clone() as Calendar
                ovulationEndCalendarFifth.add(Calendar.DATE, 1)

                val pregnancyStartDateFifth = ovulationStartCalendarFifth.clone() as Calendar
                pregnancyStartDateFifth.add(Calendar.DATE, -5)

                val pregnancyEndDateFifth = ovulationEndCalendarFifth.clone() as Calendar
                pregnancyEndDateFifth.add(Calendar.DATE, 3)

                val getDatesixth = endCycleCalendarFifth.clone() as Calendar
                getDatesixth.add(Calendar.DATE, 1)

                val startPeriodCalendarsixth = getDatesixth.clone() as Calendar
                //startPeriodCalendarsixth.add(Calendar.DATE, -1)

                val endPeriodCalendarsixth = getDatesixth.clone() as Calendar
                endPeriodCalendarsixth.add(Calendar.DATE, collapsibleCalendar.periodLength)

                val endCycleCalendarsixth = getDatesixth.clone() as Calendar
                endCycleCalendarsixth.add(Calendar.DATE, collapsibleCalendar.cycleLength -1)

                val ovulationStartCalendarsixth = endCycleCalendarsixth.clone() as Calendar
                ovulationStartCalendarsixth.add(Calendar.DATE, -13)

                val ovulationEndCalendarsixth = ovulationStartCalendarsixth.clone() as Calendar
                ovulationEndCalendarsixth.add(Calendar.DATE, 1)

                val pregnancyStartDatesixth = ovulationStartCalendarsixth.clone() as Calendar
                pregnancyStartDatesixth.add(Calendar.DATE, -5)

                val pregnancyEndDatesixth = ovulationEndCalendarsixth.clone() as Calendar
                pregnancyEndDatesixth.add(Calendar.DATE, 3)

                val getDateSeventh = endCycleCalendarsixth.clone() as Calendar
                getDateSeventh.add(Calendar.DATE, 1)

                val startPeriodCalendarSeventh = getDateSeventh.clone() as Calendar
                //startPeriodCalendarSeventh.add(Calendar.DATE, -1)

                val endPeriodCalendarSeventh = getDateSeventh.clone() as Calendar
                endPeriodCalendarSeventh.add(Calendar.DATE, collapsibleCalendar.periodLength)

                val endCycleCalendarSeventh = getDateSeventh.clone() as Calendar
                endCycleCalendarSeventh.add(Calendar.DATE, collapsibleCalendar.cycleLength -1)

                val ovulationStartCalendarSeventh = endCycleCalendarSeventh.clone() as Calendar
                ovulationStartCalendarSeventh.add(Calendar.DATE, -13)

                val ovulationEndCalendarSeventh = ovulationStartCalendarSeventh.clone() as Calendar
                ovulationEndCalendarSeventh.add(Calendar.DATE, 1)

                val pregnancyStartDateSeventh = ovulationStartCalendarSeventh.clone() as Calendar
                pregnancyStartDateSeventh.add(Calendar.DATE, -5)

                val pregnancyEndDateSeventh = ovulationEndCalendarSeventh.clone() as Calendar
                pregnancyEndDateSeventh.add(Calendar.DATE, 3)


                val eventDate = Calendar.getInstance()
                eventDate.set(Calendar.DATE, day.day)
                eventDate.set(Calendar.MONTH, day.month)
                eventDate.set(Calendar.YEAR, day.year)

                bottomLineView.visibility = View.GONE

                if (eventDate.after(collapsibleCalendar.startPeriodCalendar) && eventDate.before(
                        collapsibleCalendar.endPeriodCalendar
                    )
                ) {
                    val tempStartCalendar =
                        collapsibleCalendar.startPeriodCalendar.clone() as Calendar
                    //tempStartCalendar.add(Calendar.DATE, 1)

                    val tempEndCalendar =
                        collapsibleCalendar.endPeriodCalendar.clone() as Calendar
                    tempEndCalendar.add(Calendar.DATE, -1)
                    leftLineView.setBackgroundColor(context.resources.getColor(R.color.menturationCycle))
                    rightLineView.setBackgroundColor(context.resources.getColor(R.color.menturationCycle))
                    when (day.day) {
                        tempStartCalendar.get(Calendar.DATE) -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.INVISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                        tempEndCalendar.get(Calendar.DATE) -> {
                            leftLineView.visibility = View.VISIBLE
                            bottomLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.INVISIBLE
                        }
                        else -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                    }
                    //imgEventTag.visibility = View.VISIBLE
                    //imgEventTag.setColorFilter(event.color, PorterDuff.Mode.SRC_ATOP)
                } else if (eventDate.after(collapsibleCalendar.ovulationStartCalendar) && eventDate.before(
                        collapsibleCalendar.ovulationEndCalendar)) {
                    val tempStartCalendar =
                        collapsibleCalendar.ovulationStartCalendar.clone() as Calendar
                    // tempStartCalendar.add(Calendar.DATE, 1)

                    val tempEndCalendar =
                        collapsibleCalendar.ovulationEndCalendar.clone() as Calendar
                    tempEndCalendar.add(Calendar.DATE, -1)

                    bottomLineView.visibility = View.VISIBLE
                    leftLineView.visibility = View.VISIBLE
                    rightLineView.visibility = View.VISIBLE

                    leftLineView.setBackgroundColor(context.resources.getColor(R.color.ovalutionCycle))
                    rightLineView.setBackgroundColor(context.resources.getColor(R.color.ovalutionCycle))
                    //bottomLineView.visibility = View.GONE
                } else if (eventDate.after(collapsibleCalendar.pregnancyStartDate) && eventDate.before(
                        collapsibleCalendar.pregnancyEndDate
                    )
                ) {
                    val tempStartCalendar =
                        collapsibleCalendar.pregnancyStartDate.clone() as Calendar
                    // tempStartCalendar.add(Calendar.DATE, 1)

                    val tempEndCalendar =
                        collapsibleCalendar.pregnancyEndDate.clone() as Calendar
                    tempEndCalendar.add(Calendar.DATE, -1)
                    leftLineView.setBackgroundColor(context.resources.getColor(R.color.pregnancyCycle))
                    rightLineView.setBackgroundColor(context.resources.getColor(R.color.pregnancyCycle))
                    when (day.day) {
                        tempStartCalendar.get(Calendar.DATE) -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.INVISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                        tempEndCalendar.get(Calendar.DATE) -> {
                            leftLineView.visibility = View.VISIBLE
                            bottomLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.INVISIBLE
                        }
                        else -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                    }
                    //imgEventTag.visibility = View.VISIBLE
                    //imgEventTag.setColorFilter(event.color, PorterDuff.Mode.SRC_ATOP)
                } else if (eventDate.after(startPeriodCalendarSecond) && eventDate.before(
                        endPeriodCalendarSecond
                    )
                ) {
                    leftLineView.setBackgroundColor(context.resources.getColor(R.color.menturationCycle))

                    rightLineView.setBackgroundColor(context.resources.getColor(R.color.menturationCycle))
                    val tempStartCalendar =
                        startPeriodCalendarSecond.clone() as Calendar
                    //tempStartCalendar.add(Calendar.DATE, 1)

                    val tempEndCalendar =
                        endPeriodCalendarSecond.clone() as Calendar
                    tempEndCalendar.add(Calendar.DATE, -1)

                    when (day.day) {
                        tempStartCalendar.get(Calendar.DATE) -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.INVISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                        tempEndCalendar.get(Calendar.DATE) -> {
                            leftLineView.visibility = View.VISIBLE
                            bottomLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.INVISIBLE
                        }
                        else -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                    }
                    //imgEventTag.visibility = View.VISIBLE
                    //imgEventTag.setColorFilter(event.color, PorterDuff.Mode.SRC_ATOP)
                } else if (eventDate.after(ovulationStartCalendarSecond) && eventDate.before(
                        ovulationEndCalendarSecond)) {
                    val tempStartCalendar =
                        ovulationStartCalendarSecond.clone() as Calendar
                    // tempStartCalendar.add(Calendar.DATE, 1)

                    val tempEndCalendar =
                        ovulationEndCalendarSecond.clone() as Calendar
                    tempEndCalendar.add(Calendar.DATE, -1)
                    bottomLineView.visibility = View.VISIBLE
                    leftLineView.visibility = View.VISIBLE
                    rightLineView.visibility = View.VISIBLE
                    leftLineView.setBackgroundColor(context.resources.getColor(R.color.ovalutionCycle))
                    rightLineView.setBackgroundColor(context.resources.getColor(R.color.ovalutionCycle))
                    //bottomLineView.visibility = View.GONE
                } else if (eventDate.after(pregnancyStartDateSecond) && eventDate.before(
                        pregnancyEndDateSecond
                    )
                ) {
                    val tempStartCalendar =
                        pregnancyStartDateSecond.clone() as Calendar
                    // tempStartCalendar.add(Calendar.DATE, 1)

                    val tempEndCalendar =
                        pregnancyEndDateSecond.clone() as Calendar
                    tempEndCalendar.add(Calendar.DATE, -1)
                    leftLineView.setBackgroundColor(context.resources.getColor(R.color.pregnancyCycle))
                    rightLineView.setBackgroundColor(context.resources.getColor(R.color.pregnancyCycle))
                    when (day.day) {
                        tempStartCalendar.get(Calendar.DATE) -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.INVISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                        tempEndCalendar.get(Calendar.DATE) -> {
                            leftLineView.visibility = View.VISIBLE
                            bottomLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.INVISIBLE
                        }
                        else -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                    }
                    //imgEventTag.visibility = View.VISIBLE
                    //imgEventTag.setColorFilter(event.color, PorterDuff.Mode.SRC_ATOP)
                }else if (eventDate.after(startPeriodCalendarThird) && eventDate.before(
                        endPeriodCalendarThird
                    )
                ) {
                    leftLineView.setBackgroundColor(context.resources.getColor(R.color.menturationCycle))
                    rightLineView.setBackgroundColor(context.resources.getColor(R.color.menturationCycle))
                    val tempStartCalendar =
                        startPeriodCalendarThird.clone() as Calendar
                    //tempStartCalendar.add(Calendar.DATE, 1)

                    val tempEndCalendar =
                        endPeriodCalendarThird.clone() as Calendar
                    tempEndCalendar.add(Calendar.DATE, -1)

                    when (day.day) {
                        tempStartCalendar.get(Calendar.DATE) -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.INVISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                        tempEndCalendar.get(Calendar.DATE) -> {
                            leftLineView.visibility = View.VISIBLE
                            bottomLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.INVISIBLE
                        }
                        else -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                    }
                    //imgEventTag.visibility = View.VISIBLE
                    //imgEventTag.setColorFilter(event.color, PorterDuff.Mode.SRC_ATOP)
                } else if (eventDate.after(ovulationStartCalendarThird) && eventDate.before(
                        ovulationEndCalendarThird)) {
                    val tempStartCalendar =
                        ovulationStartCalendarThird.clone() as Calendar
                    // tempStartCalendar.add(Calendar.DATE, 1)

                    val tempEndCalendar =
                        ovulationEndCalendarThird.clone() as Calendar
                    tempEndCalendar.add(Calendar.DATE, -1)
                    bottomLineView.visibility = View.VISIBLE
                    leftLineView.visibility = View.VISIBLE
                    rightLineView.visibility = View.VISIBLE
                    leftLineView.setBackgroundColor(context.resources.getColor(R.color.ovalutionCycle))
                    rightLineView.setBackgroundColor(context.resources.getColor(R.color.ovalutionCycle))
                    //bottomLineView.visibility = View.GONE
                } else if (eventDate.after(pregnancyStartDateThird) && eventDate.before(
                        pregnancyEndDateThird
                    )
                ) {
                    val tempStartCalendar =
                        pregnancyStartDateThird.clone() as Calendar
                    // tempStartCalendar.add(Calendar.DATE, 1)

                    val tempEndCalendar =
                        pregnancyEndDateThird.clone() as Calendar
                    tempEndCalendar.add(Calendar.DATE, -1)
                    leftLineView.setBackgroundColor(context.resources.getColor(R.color.pregnancyCycle))
                    rightLineView.setBackgroundColor(context.resources.getColor(R.color.pregnancyCycle))
                    when (day.day) {
                        tempStartCalendar.get(Calendar.DATE) -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.INVISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                        tempEndCalendar.get(Calendar.DATE) -> {
                            leftLineView.visibility = View.VISIBLE
                            bottomLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.INVISIBLE
                        }
                        else -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                    }
                    //imgEventTag.visibility = View.VISIBLE
                    //imgEventTag.setColorFilter(event.color, PorterDuff.Mode.SRC_ATOP)
                }
                else if (eventDate.after(startPeriodCalendarFourth) && eventDate.before(
                        endPeriodCalendarFourth
                    )
                ) {
                    leftLineView.setBackgroundColor(context.resources.getColor(R.color.menturationCycle))
                    rightLineView.setBackgroundColor(context.resources.getColor(R.color.menturationCycle))
                    val tempStartCalendar =
                        startPeriodCalendarFourth.clone() as Calendar
                    //tempStartCalendar.add(Calendar.DATE, 1)

                    val tempEndCalendar =
                        endPeriodCalendarFourth.clone() as Calendar
                    tempEndCalendar.add(Calendar.DATE, -1)

                    when (day.day) {
                        tempStartCalendar.get(Calendar.DATE) -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.INVISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                        tempEndCalendar.get(Calendar.DATE) -> {
                            leftLineView.visibility = View.VISIBLE
                            bottomLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.INVISIBLE
                        }
                        else -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                    }
                    //imgEventTag.visibility = View.VISIBLE
                    //imgEventTag.setColorFilter(event.color, PorterDuff.Mode.SRC_ATOP)
                } else if (eventDate.after(ovulationStartCalendarFourth) && eventDate.before(
                        ovulationEndCalendarFourth)) {
                    val tempStartCalendar =
                        ovulationStartCalendarFourth.clone() as Calendar
                    // tempStartCalendar.add(Calendar.DATE, 1)

                    val tempEndCalendar =
                        ovulationEndCalendarFourth.clone() as Calendar
                    tempEndCalendar.add(Calendar.DATE, -1)
                    bottomLineView.visibility = View.VISIBLE
                    leftLineView.visibility = View.VISIBLE
                    rightLineView.visibility = View.VISIBLE
                    leftLineView.setBackgroundColor(context.resources.getColor(R.color.ovalutionCycle))
                    rightLineView.setBackgroundColor(context.resources.getColor(R.color.ovalutionCycle))
                    //bottomLineView.visibility = View.GONE
                } else if (eventDate.after(pregnancyStartDateFourth) && eventDate.before(
                        pregnancyEndDateFourth
                    )
                ) {
                    val tempStartCalendar =
                        pregnancyStartDateFourth.clone() as Calendar
                    // tempStartCalendar.add(Calendar.DATE, 1)

                    val tempEndCalendar =
                        pregnancyEndDateFourth.clone() as Calendar
                    tempEndCalendar.add(Calendar.DATE, -1)
                    leftLineView.setBackgroundColor(context.resources.getColor(R.color.pregnancyCycle))
                    rightLineView.setBackgroundColor(context.resources.getColor(R.color.pregnancyCycle))
                    when (day.day) {
                        tempStartCalendar.get(Calendar.DATE) -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.INVISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                        tempEndCalendar.get(Calendar.DATE) -> {
                            leftLineView.visibility = View.VISIBLE
                            bottomLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.INVISIBLE
                        }
                        else -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                    }
                    //imgEventTag.visibility = View.VISIBLE
                    //imgEventTag.setColorFilter(event.color, PorterDuff.Mode.SRC_ATOP)
                }
                else if (eventDate.after(startPeriodCalendarFifth) && eventDate.before(
                        endPeriodCalendarFifth
                    )
                ) {
                    leftLineView.setBackgroundColor(context.resources.getColor(R.color.menturationCycle))
                    rightLineView.setBackgroundColor(context.resources.getColor(R.color.menturationCycle))
                    val tempStartCalendar =
                        startPeriodCalendarFifth.clone() as Calendar
                    //tempStartCalendar.add(Calendar.DATE, 1)

                    val tempEndCalendar =
                        endPeriodCalendarFifth.clone() as Calendar
                    tempEndCalendar.add(Calendar.DATE, -1)

                    when (day.day) {
                        tempStartCalendar.get(Calendar.DATE) -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.INVISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                        tempEndCalendar.get(Calendar.DATE) -> {
                            leftLineView.visibility = View.VISIBLE
                            bottomLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.INVISIBLE
                        }
                        else -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                    }
                    //imgEventTag.visibility = View.VISIBLE
                    //imgEventTag.setColorFilter(event.color, PorterDuff.Mode.SRC_ATOP)
                } else if (eventDate.after(ovulationStartCalendarFifth) && eventDate.before(
                        ovulationEndCalendarFifth)) {
                    val tempStartCalendar =
                        ovulationStartCalendarFifth.clone() as Calendar
                    // tempStartCalendar.add(Calendar.DATE, 1)

                    val tempEndCalendar =
                        ovulationEndCalendarFifth.clone() as Calendar
                    tempEndCalendar.add(Calendar.DATE, -1)
                    bottomLineView.visibility = View.VISIBLE
                    leftLineView.visibility = View.VISIBLE
                    rightLineView.visibility = View.VISIBLE
                    leftLineView.setBackgroundColor(context.resources.getColor(R.color.ovalutionCycle))
                    rightLineView.setBackgroundColor(context.resources.getColor(R.color.ovalutionCycle))
                    //bottomLineView.visibility = View.GONE
                } else if (eventDate.after(pregnancyStartDateFifth) && eventDate.before(
                        pregnancyEndDateFifth
                    )
                ) {
                    val tempStartCalendar =
                        pregnancyStartDateFifth.clone() as Calendar
                    // tempStartCalendar.add(Calendar.DATE, 1)

                    val tempEndCalendar =
                        pregnancyEndDateFifth.clone() as Calendar
                    tempEndCalendar.add(Calendar.DATE, -1)
                    leftLineView.setBackgroundColor(context.resources.getColor(R.color.pregnancyCycle))
                    rightLineView.setBackgroundColor(context.resources.getColor(R.color.pregnancyCycle))
                    when (day.day) {
                        tempStartCalendar.get(Calendar.DATE) -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.INVISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                        tempEndCalendar.get(Calendar.DATE) -> {
                            leftLineView.visibility = View.VISIBLE
                            bottomLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.INVISIBLE
                        }
                        else -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                    }
                    //imgEventTag.visibility = View.VISIBLE
                    //imgEventTag.setColorFilter(event.color, PorterDuff.Mode.SRC_ATOP)
                }
                else if (eventDate.after(startPeriodCalendarsixth) && eventDate.before(
                        endPeriodCalendarsixth
                    )
                ) {
                    leftLineView.setBackgroundColor(context.resources.getColor(R.color.menturationCycle))
                    rightLineView.setBackgroundColor(context.resources.getColor(R.color.menturationCycle))
                    val tempStartCalendar =
                        startPeriodCalendarsixth.clone() as Calendar
                    //tempStartCalendar.add(Calendar.DATE, 1)

                    val tempEndCalendar =
                        endPeriodCalendarsixth.clone() as Calendar
                    tempEndCalendar.add(Calendar.DATE, -1)

                    when (day.day) {
                        tempStartCalendar.get(Calendar.DATE) -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.INVISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                        tempEndCalendar.get(Calendar.DATE) -> {
                            leftLineView.visibility = View.VISIBLE
                            bottomLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.INVISIBLE
                        }
                        else -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                    }
                    //imgEventTag.visibility = View.VISIBLE
                    //imgEventTag.setColorFilter(event.color, PorterDuff.Mode.SRC_ATOP)
                } else if (eventDate.after(ovulationStartCalendarsixth) && eventDate.before(
                        ovulationEndCalendarsixth)) {
                    val tempStartCalendar =
                        ovulationStartCalendarsixth.clone() as Calendar
                    // tempStartCalendar.add(Calendar.DATE, 1)

                    val tempEndCalendar =
                        ovulationEndCalendarsixth.clone() as Calendar
                    tempEndCalendar.add(Calendar.DATE, -1)
                    bottomLineView.visibility = View.VISIBLE
                    leftLineView.visibility = View.VISIBLE
                    rightLineView.visibility = View.VISIBLE
                    leftLineView.setBackgroundColor(context.resources.getColor(R.color.ovalutionCycle))
                    rightLineView.setBackgroundColor(context.resources.getColor(R.color.ovalutionCycle))
                    //bottomLineView.visibility = View.GONE
                } else if (eventDate.after(pregnancyStartDatesixth) && eventDate.before(
                        pregnancyEndDatesixth
                    )
                ) {
                    val tempStartCalendar =
                        pregnancyStartDatesixth.clone() as Calendar
                    // tempStartCalendar.add(Calendar.DATE, 1)

                    val tempEndCalendar =
                        pregnancyEndDatesixth.clone() as Calendar
                    tempEndCalendar.add(Calendar.DATE, -1)
                    leftLineView.setBackgroundColor(context.resources.getColor(R.color.pregnancyCycle))
                    rightLineView.setBackgroundColor(context.resources.getColor(R.color.pregnancyCycle))
                    when (day.day) {
                        tempStartCalendar.get(Calendar.DATE) -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.INVISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                        tempEndCalendar.get(Calendar.DATE) -> {
                            leftLineView.visibility = View.VISIBLE
                            bottomLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.INVISIBLE
                        }
                        else -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                    }
                    //imgEventTag.visibility = View.VISIBLE
                    //imgEventTag.setColorFilter(event.color, PorterDuff.Mode.SRC_ATOP)
                }

                else if (eventDate.after(startPeriodCalendarSeventh) && eventDate.before(
                        endPeriodCalendarSeventh
                    )
                ) {
                    leftLineView.setBackgroundColor(context.resources.getColor(R.color.menturationCycle))
                    rightLineView.setBackgroundColor(context.resources.getColor(R.color.menturationCycle))
                    val tempStartCalendar =
                        startPeriodCalendarSeventh.clone() as Calendar
                    //tempStartCalendar.add(Calendar.DATE, 1)

                    val tempEndCalendar =
                        endPeriodCalendarSeventh.clone() as Calendar
                    tempEndCalendar.add(Calendar.DATE, -1)

                    when (day.day) {
                        tempStartCalendar.get(Calendar.DATE) -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.INVISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                        tempEndCalendar.get(Calendar.DATE) -> {
                            leftLineView.visibility = View.VISIBLE
                            bottomLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.INVISIBLE
                        }
                        else -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                    }
                    //imgEventTag.visibility = View.VISIBLE
                    //imgEventTag.setColorFilter(event.color, PorterDuff.Mode.SRC_ATOP)
                } else if (eventDate.after(ovulationStartCalendarSeventh) && eventDate.before(
                        ovulationEndCalendarSeventh)) {
                    val tempStartCalendar =
                        ovulationStartCalendarSeventh.clone() as Calendar
                    // tempStartCalendar.add(Calendar.DATE, 1)

                    val tempEndCalendar =
                        ovulationEndCalendarSeventh.clone() as Calendar
                    tempEndCalendar.add(Calendar.DATE, -1)
                    bottomLineView.visibility = View.VISIBLE
                    leftLineView.visibility = View.VISIBLE
                    rightLineView.visibility = View.VISIBLE
                    leftLineView.setBackgroundColor(context.resources.getColor(R.color.ovalutionCycle))
                    rightLineView.setBackgroundColor(context.resources.getColor(R.color.ovalutionCycle))
                    //bottomLineView.visibility = View.GONE
                } else if (eventDate.after(pregnancyStartDateSeventh) && eventDate.before(
                        pregnancyEndDateSeventh
                    )
                ) {
                    val tempStartCalendar =
                        pregnancyStartDateSeventh.clone() as Calendar
                    // tempStartCalendar.add(Calendar.DATE, 1)

                    val tempEndCalendar =
                        pregnancyEndDateSeventh.clone() as Calendar
                    tempEndCalendar.add(Calendar.DATE, -1)
                    leftLineView.setBackgroundColor(context.resources.getColor(R.color.pregnancyCycle))
                    rightLineView.setBackgroundColor(context.resources.getColor(R.color.pregnancyCycle))
                    when (day.day) {
                        tempStartCalendar.get(Calendar.DATE) -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.INVISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                        tempEndCalendar.get(Calendar.DATE) -> {
                            leftLineView.visibility = View.VISIBLE
                            bottomLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.INVISIBLE
                        }
                        else -> {
                            bottomLineView.visibility = View.VISIBLE
                            leftLineView.visibility = View.VISIBLE
                            rightLineView.visibility = View.VISIBLE
                        }
                    }
                    //imgEventTag.visibility = View.VISIBLE
                    //imgEventTag.setColorFilter(event.color, PorterDuff.Mode.SRC_ATOP)
                }
                else {
                    bottomLineView.visibility = View.GONE
                }
            }




            /* for (j in mEventList.indices) {
                 val event = mEventList[j]

                 val calendarEdited = collapsibleCalendar.endCycleCalendar.clone() as Calendar


                 for (monthOffset in 0 until 5) {

                     calendarEdited.add(Calendar.MONTH, 1)

                     // Perform operations with the updated calendar
                     // For example, you can print the month and year
                     val month = calendarEdited.get(Calendar.MONTH) + 1 // Calendar.MONTH is zero-based
                     val year = calendarEdited.get(Calendar.YEAR)

                     println("Month: $month, Year: $year")




                     val getDateSecond = calendarEdited.clone() as Calendar
                     getDateSecond.add(Calendar.DATE, 1)


                     val startPeriodCalendarSecond = getDateSecond.clone() as Calendar
                     //startPeriodCalendarSecond.add(Calendar.DATE, -1)

                     val endPeriodCalendarSecond = getDateSecond.clone() as Calendar
                     endPeriodCalendarSecond.add(Calendar.DATE, collapsibleCalendar.periodLength)

                     val endCycleCalendarSecond = getDateSecond.clone() as Calendar
                     endCycleCalendarSecond.add(Calendar.DATE, collapsibleCalendar.cycleLength - 1)

                     val ovulationStartCalendarSecond = endCycleCalendarSecond.clone() as Calendar
                     ovulationStartCalendarSecond.add(Calendar.DATE, -13)

                     val ovulationEndCalendarSecond =
                         ovulationStartCalendarSecond.clone() as Calendar
                     ovulationEndCalendarSecond.add(Calendar.DATE, 1)

                     val pregnancyStartDateSecond = ovulationStartCalendarSecond.clone() as Calendar
                     pregnancyStartDateSecond.add(Calendar.DATE, -5)

                     val pregnancyEndDateSecond = ovulationEndCalendarSecond.clone() as Calendar
                     pregnancyEndDateSecond.add(Calendar.DATE, 3)

                     for (monthOffset in 0 until 5) {
                     val eventDate = Calendar.getInstance()
                     eventDate.set(Calendar.DATE, day.day)
                     eventDate.set(Calendar.MONTH, day.month)
                     eventDate.set(Calendar.YEAR, day.year)

                     bottomLineView.visibility = View.GONE

                     val baseStartPeriodCalendar = collapsibleCalendar.startPeriodCalendar.clone() as Calendar
                     val baseEndPeriodCalendar = collapsibleCalendar.endPeriodCalendar.clone() as Calendar
                     val baseOvulationStartCalendar = collapsibleCalendar.ovulationStartCalendar.clone() as Calendar
                     val baseOvulationEndCalendar = collapsibleCalendar.ovulationEndCalendar.clone() as Calendar
                     val basePregnancyStartDate = collapsibleCalendar.pregnancyStartDate.clone() as Calendar
                     val basePregnancyEndDate = collapsibleCalendar.pregnancyEndDate.clone() as Calendar


                         // Clone and adjust base calendars for the current month
                         val startPeriodCalendar = baseStartPeriodCalendar.clone() as Calendar
                         val endPeriodCalendar = baseEndPeriodCalendar.clone() as Calendar
                         val ovulationStartCalendar = baseOvulationStartCalendar.clone() as Calendar
                         val ovulationEndCalendar = baseOvulationEndCalendar.clone() as Calendar
                         val pregnancyStartDate = basePregnancyStartDate.clone() as Calendar
                         val pregnancyEndDate = basePregnancyEndDate.clone() as Calendar

                         // Add the month offset to each calendar
                         startPeriodCalendar.add(Calendar.MONTH, monthOffset)
                         endPeriodCalendar.add(Calendar.MONTH, monthOffset)
                         ovulationStartCalendar.add(Calendar.MONTH, monthOffset)
                         ovulationEndCalendar.add(Calendar.MONTH, monthOffset)
                         pregnancyStartDate.add(Calendar.MONTH, monthOffset)
                         pregnancyEndDate.add(Calendar.MONTH, monthOffset)

                         // Assume `eventDate` and `day.day` are available here
                         if (eventDate.after(startPeriodCalendar) && eventDate.before(endPeriodCalendar)) {
                             val tempStartCalendar = startPeriodCalendar.clone() as Calendar
                             val tempEndCalendar = endPeriodCalendar.clone() as Calendar
                             tempEndCalendar.add(Calendar.DATE, -1)
                             leftLineView.setBackgroundColor(context.resources.getColor(R.color.menturationCycle))
                             rightLineView.setBackgroundColor(context.resources.getColor(R.color.menturationCycle))
                             when (day.day) {
                                 tempStartCalendar.get(Calendar.DATE) -> {
                                     bottomLineView.visibility = View.VISIBLE
                                     leftLineView.visibility = View.INVISIBLE
                                     rightLineView.visibility = View.VISIBLE
                                 }
                                 tempEndCalendar.get(Calendar.DATE) -> {
                                     leftLineView.visibility = View.VISIBLE
                                     bottomLineView.visibility = View.VISIBLE
                                     rightLineView.visibility = View.INVISIBLE
                                 }
                                 else -> {
                                     bottomLineView.visibility = View.VISIBLE
                                     leftLineView.visibility = View.VISIBLE
                                     rightLineView.visibility = View.VISIBLE
                                 }
                             }
                         } else if (eventDate.after(ovulationStartCalendar) && eventDate.before(ovulationEndCalendar)) {
                             val tempStartCalendar = ovulationStartCalendar.clone() as Calendar
                             val tempEndCalendar = ovulationEndCalendar.clone() as Calendar
                             tempEndCalendar.add(Calendar.DATE, -1)
                             bottomLineView.visibility = View.VISIBLE
                             leftLineView.visibility = View.VISIBLE
                             rightLineView.visibility = View.VISIBLE
                             leftLineView.setBackgroundColor(context.resources.getColor(R.color.ovalutionCycle))
                             rightLineView.setBackgroundColor(context.resources.getColor(R.color.ovalutionCycle))
                         } else if (eventDate.after(pregnancyStartDate) && eventDate.before(pregnancyEndDate)) {
                             val tempStartCalendar = pregnancyStartDate.clone() as Calendar
                             val tempEndCalendar = pregnancyEndDate.clone() as Calendar
                             tempEndCalendar.add(Calendar.DATE, -1)
                             leftLineView.setBackgroundColor(context.resources.getColor(R.color.pregnancyCycle))
                             rightLineView.setBackgroundColor(context.resources.getColor(R.color.pregnancyCycle))
                             when (day.day) {
                                 tempStartCalendar.get(Calendar.DATE) -> {
                                     bottomLineView.visibility = View.VISIBLE
                                     leftLineView.visibility = View.INVISIBLE
                                     rightLineView.visibility = View.VISIBLE
                                 }
                                 tempEndCalendar.get(Calendar.DATE) -> {
                                     leftLineView.visibility = View.VISIBLE
                                     bottomLineView.visibility = View.VISIBLE
                                     rightLineView.visibility = View.INVISIBLE
                                 }
                                 else -> {
                                     bottomLineView.visibility = View.VISIBLE
                                     leftLineView.visibility = View.VISIBLE
                                     rightLineView.visibility = View.VISIBLE
                                 }
                             }
                         } else {
                             bottomLineView.visibility = View.GONE
                         }
                     }
                 }
             }*/
            mItemList.add(day)
            mViewList.add(view)
        }
    }
}

package com.mumjolandia.android.ui.planner.plan

class PlannerTaskComparator : Comparator<PlannerTask> {
    override fun compare(o1: PlannerTask?, o2: PlannerTask?): Int {
        when {
            o1?.time?.substring(0,2)?.toInt()!! == o2?.time?.substring(0,2)?.toInt()!! -> {
                if (o1.time.substring(3,5).toInt() > o2.time.substring(3,5).toInt()){
                    return 1
                }
                if (o1.time.substring(3,5).toInt() < o2.time.substring(3,5).toInt()){
                    return -1
                }
                return 0
            }
            o1.time.substring(0,2).toInt() < o2.time.substring(0,2).toInt() -> return -1
            else -> return 1
        }
    }
}

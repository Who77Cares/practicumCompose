package com.example.practicumcompose.charts

/**
 * Один препарат / группа лечения.
 *
 * @param id      уникальный строковый ключ
 * @param name    отображаемое название
 * @param color   hex-цвет для UI (#RRGGBB)
 * @param data    измерения: сутки → площадь раны, мм²
 */
data class Treatment(
    val id: String,
    val name: String,
    val color: String,
    val data: Map<Int, Double>
) {
    /** Площадь раны в заданные сутки, или null если нет измерения. */
    fun valueAt(day: Int): Double? = data[day]

    /**
     * Процент снижения площади от [fromDay] до [toDay].
     * Положительное значение = улучшение.
     */
    fun reductionPercent(fromDay: Int, toDay: Int): Double? {
        val from = valueAt(fromDay) ?: return null
        val to   = valueAt(toDay)   ?: return null
        if (from == 0.0) return null
        return (1.0 - to / from) * 100.0
    }

    /** Значение в последние зафиксированные сутки. */
    fun finalValue(): Double {
        val lastDay = data.keys.max()
        return data.getValue(lastDay)
    }

    /**
     * Средняя скорость заживления (мм²/сут) между первым и последним замером.
     * Положительное = площадь уменьшается.
     */
    fun averageHealingRate(): Double {
        val sortedDays = data.keys.sorted()
        if (sortedDays.size < 2) return 0.0
        val firstDay = sortedDays.first()
        val lastDay  = sortedDays.last()
        return (data.getValue(firstDay) - data.getValue(lastDay)) /
                (lastDay - firstDay).toDouble()
    }

    /** Строка для отладки / логов. */
    override fun toString(): String =
        "Treatment(id=$id, final=${finalValue()} мм², reduction=${
            reductionPercent(data.keys.min(), data.keys.max())
                ?.let { "%.1f%%".format(it) } ?: "n/a"
        })"
}

// ─────────────────────────────────────────────
// Summary DTO (для таблиц / экспорта)
// ─────────────────────────────────────────────

data class TreatmentSummary(
    val name: String,
    val color: String,
    val initialValue: Double,
    val finalValue: Double,
    val reductionPercent: Double?,
    val avgHealingRate: Double
)

// ─────────────────────────────────────────────
// Dataset — object-синглтон
// ─────────────────────────────────────────────

/**
 * Единственный источник правды для данных по заживлению ран.
 *
 * Использование:
 *   WoundHealingDataset.bestFinalTreatment()
 *   WoundHealingDataset.summaryRows()
 */
object WoundHealingDataset {

    val days: List<Int> = listOf(1, 3, 5, 7, 9, 14)

    val treatments: List<Treatment> = listOf(
        Treatment(
            id    = "isotonic",
            name  = "Изотонический раствор",
            color = "#D85A30",
            data  = mapOf(1 to 400.0, 3 to 380.0, 5 to 330.0, 7 to 280.0, 9 to 220.0, 14 to 215.0)
        ),
        Treatment(
            id    = "iron",
            name  = "Наночастицы железа",
            color = "#EF9F27",
            data  = mapOf(1 to 400.0, 3 to 425.0, 5 to 385.0, 7 to 140.0, 9 to 130.0, 14 to 85.0)
        ),
        Treatment(
            id    = "copper",
            name  = "Наночастицы меди",
            color = "#3C3489",
            data  = mapOf(1 to 400.0, 3 to 330.0, 5 to 205.0, 7 to 185.0, 9 to 145.0, 14 to 305.0)
        ),
        Treatment(
            id    = "zinc",
            name  = "Наночастицы цинка",
            color = "#D4537E",
            data  = mapOf(1 to 400.0, 3 to 335.0, 5 to 330.0, 7 to 280.0, 9 to 215.0, 14 to 215.0)
        )
    )

    // ── Запросы ──────────────────────────────

    /** Препарат по id, или null. */
    fun findById(id: String): Treatment? =
        treatments.find { it.id == id }

    /** Препарат с наименьшей площадью раны в заданные сутки. */
    fun bestTreatmentAt(day: Int): Treatment? =
        treatments
            .filter { it.valueAt(day) != null }
            .minByOrNull { it.valueAt(day)!! }

    /** Препарат с наименьшей финальной площадью раны. */
    fun bestFinalTreatment(): Treatment =
        treatments.minBy { it.finalValue() }

    /** Сводные строки для таблицы / экспорта. */
    fun summaryRows(): List<TreatmentSummary> = treatments.map { t ->
        TreatmentSummary(
            name             = t.name,
            color            = t.color,
            initialValue     = t.valueAt(days.first()) ?: 0.0,
            finalValue       = t.finalValue(),
            reductionPercent = t.reductionPercent(days.first(), days.last()),
            avgHealingRate   = t.averageHealingRate()
        )
    }

    /** Данные в виде матрицы [день → [id → значение]] удобны для графиков. */
    fun toMatrix(): Map<Int, Map<String, Double?>> =
        days.associateWith { day ->
            treatments.associate { t -> t.id to t.valueAt(day) }
        }
}

// ─────────────────────────────────────────────
// Demo main
// ─────────────────────────────────────────────

fun main() {
    println("=== Сводка по препаратам ===\n")
    WoundHealingDataset.summaryRows().forEach { row ->
        println(
            "%-28s | начало: %5.0f мм² | день 14: %5.0f мм² | снижение: %5.1f%% | скорость: %.1f мм²/сут".format(
                row.name,
                row.initialValue,
                row.finalValue,
                row.reductionPercent ?: 0.0,
                row.avgHealingRate
            )
        )
    }

    println("\n=== Лучший результат ===")
    val best = WoundHealingDataset.bestFinalTreatment()
    println(best)

    println("\n=== Лидер на день 7 ===")
    println(WoundHealingDataset.bestTreatmentAt(7))
}
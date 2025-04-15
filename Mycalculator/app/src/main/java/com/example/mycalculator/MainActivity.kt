package com.example.mycalculator

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {

    // Declare UI components
    private lateinit var btnBack: ImageButton
    private lateinit var radioEnergetic: RadioButton
    private lateinit var radioHappy: RadioButton
    private lateinit var radioNeutral: RadioButton
    private lateinit var radioTired: RadioButton
    private lateinit var radioSad: RadioButton
    private lateinit var radioAngry: RadioButton
    private lateinit var radioAnxious: RadioButton

    private lateinit var cardBreakfast: CardView
    private lateinit var cardLunch: CardView
    private lateinit var cardDinner: CardView

    private lateinit var seekBarSleep: SeekBar
    private lateinit var tvSleepHours: TextView

    private lateinit var switchActivity: Switch
    private lateinit var btnCalculate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI components
        initializeViews()

        // Set up listeners
        setupListeners()
    }

    private fun initializeViews() {
        btnBack = findViewById(R.id.btnBack)

        // Mood radio buttons
        radioEnergetic = findViewById(R.id.radioEnergetic)
        radioHappy = findViewById(R.id.radioHappy)
        radioNeutral = findViewById(R.id.radioNeutral)
        radioTired = findViewById(R.id.radioTired)
        radioSad = findViewById(R.id.radioSad)
        radioAngry = findViewById(R.id.radioAngry)
        radioAnxious = findViewById(R.id.radioAnxious)

        // Meal cards
        cardBreakfast = findViewById(R.id.cardBreakfast)
        cardLunch = findViewById(R.id.cardLunch)
        cardDinner = findViewById(R.id.cardDinner)

        // Sleep section
        seekBarSleep = findViewById(R.id.seekBarSleep)
        tvSleepHours = findViewById(R.id.tvSleepHours)

        // Activity section
        switchActivity = findViewById(R.id.switchActivity)

        // Calculate button
        btnCalculate = findViewById(R.id.btnCalculate)
    }

    private fun setupListeners() {
        // Back button listener
        btnBack.setOnClickListener {
            finish()
        }

        // Sleep seekbar listener
        seekBarSleep.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // Update the text view with current progress
                tvSleepHours.text = "$progress hours"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Not needed for this implementation
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Not needed for this implementation
            }
        })

        // Meal card click listeners
        cardBreakfast.setOnClickListener {
            toggleCardSelection(cardBreakfast)
        }

        cardLunch.setOnClickListener {
            toggleCardSelection(cardLunch)
        }

        cardDinner.setOnClickListener {
            toggleCardSelection(cardDinner)
        }

        // Calculate button listener
        btnCalculate.setOnClickListener {
            calculateProductivity()
        }
    }

    private fun toggleCardSelection(cardView: CardView) {
        // Toggle between selected and unselected state
        if (cardView.cardBackgroundColor.defaultColor == resources.getColor(R.color.color1)) {
            // If already selected, deselect
            cardView.setCardBackgroundColor(resources.getColor(android.R.color.darker_gray))
        } else {
            // If not selected, select
            cardView.setCardBackgroundColor(resources.getColor(R.color.color1))
        }
    }

    private fun calculateProductivity() {
        // Collect data from UI components
        val sleepHours = seekBarSleep.progress
        val hasPhysicalActivity = switchActivity.isChecked

        // Check which mood is selected
        val selectedMood = when {
            radioEnergetic.isChecked -> "Energetic"
            radioHappy.isChecked -> "Happy"
            radioNeutral.isChecked -> "Neutral"
            radioTired.isChecked -> "Tired"
            radioSad.isChecked -> "Sad"
            radioAngry.isChecked -> "Angry"
            radioAnxious.isChecked -> "Anxious"
            else -> "Not selected"
        }

        // Check which meals are taken
        val breakfastTaken = cardBreakfast.cardBackgroundColor.defaultColor == resources.getColor(R.color.color1)
        val lunchTaken = cardLunch.cardBackgroundColor.defaultColor == resources.getColor(R.color.color1)
        val dinnerTaken = cardDinner.cardBackgroundColor.defaultColor == resources.getColor(R.color.color1)

        // Calculate productivity score (simple example)
        var productivityScore = 0

        // Sleep contribution (0-10 points)
        productivityScore += when {
            sleepHours < 4 -> 0
            sleepHours < 6 -> 3
            sleepHours < 8 -> 7
            sleepHours <= 10 -> 10
            else -> 5  // Too much sleep
        }

        // Mood contribution (0-10 points)
        productivityScore += when (selectedMood) {
            "Energetic" -> 10
            "Happy" -> 8
            "Neutral" -> 5
            "Tired" -> 2
            "Sad" -> 1
            "Angry" -> 0
            "Anxious" -> 0
            else -> 0
        }

        // Meals contribution (0-15 points, 5 per meal)
        if (breakfastTaken) productivityScore += 5
        if (lunchTaken) productivityScore += 5
        if (dinnerTaken) productivityScore += 5

        // Activity contribution (0-10 points)
        if (hasPhysicalActivity) productivityScore += 10

        // Calculate percentage (out of 45 possible points)
        val productivityPercentage = (productivityScore * 100 / 45)

        // Show results in popup dialog
        showResultsDialog(productivityPercentage)
    }

    private fun showResultsDialog(score: Int) {
        // Create custom dialog
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.productivity_results_layout)

        // Set dialog width to match parent (screen width)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams

        // Initialize dialog views
        val tvProductivityPercentage = dialog.findViewById<TextView>(R.id.tvProductivityPercentage)
        val tvProductivityLevel = dialog.findViewById<TextView>(R.id.tvProductivityLevel)
        val progressProductivity = dialog.findViewById<ProgressBar>(R.id.progressProductivity)
        val btnViewSuggestions = dialog.findViewById<Button>(R.id.btnViewSuggestions)

        // Display productivity score
        tvProductivityPercentage.text = "$score%"
        progressProductivity.progress = score

        // Set productivity level based on score
        val productivityLevel = when {
            score < 30 -> "Low"
            score < 60 -> "Medium"
            else -> "High"
        }
        tvProductivityLevel.text = "Productivity Level: $productivityLevel"

        // Set up suggestions button
        btnViewSuggestions.setOnClickListener {
            showSuggestionsDialog(score)
            dialog.dismiss()
        }

        // Show dialog
        dialog.show()
    }

    private fun showSuggestionsDialog(score: Int) {
        // Create custom dialog for suggestions
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.suggestion_item_layout)

        // Set dialog width to match parent (screen width)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams

        // TODO: Implement the suggestions dialog based on score
        // This would require having a suggestions_layout.xml file

        // For now, we'll just create a simple dialog with a close button
        val btnClose = dialog.findViewById<Button>(R.id.btnClosesuggestions)
        btnClose?.setOnClickListener {
            dialog.dismiss()
        }

        // Show dialog
        dialog.show()
    }
}
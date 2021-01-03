package com.hardzei.coronavirusapp.view.screens.helpscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.hardzei.coronavirusapp.INTRO_STRING_OBJECT
import com.hardzei.coronavirusapp.R
import kotlinx.android.synthetic.main.view_pager_help_content.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ViewPagerContentHelpFragment : Fragment() {

    private lateinit var job: Job

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater
            .inflate(
                R.layout.view_pager_help_content,
                container,
                false
            )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.takeIf { it.containsKey(INTRO_STRING_OBJECT) }?.apply {
            changeColor(getStringArray(INTRO_STRING_OBJECT)!![0])
        }

        initListeners()
        job = startAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
    }

    private fun startAnimation() = CoroutineScope(Main).launch {
        while (true) {
            if (skip_text_view.alpha == 0.0f) {
                skip_text_view.animate().alpha(1.0f).setDuration(DELAY_FOR_ANIMATION)
            } else {
                skip_text_view.animate().alpha(0.0f).setDuration(DELAY_FOR_ANIMATION)
            }
            delay(DELAY_BETWEEN_ANIMATIONS)
        }
    }

    private fun initListeners() {
        skip_text_view
            .setOnClickListener(
                Navigation
                    .createNavigateOnClickListener(
                        R.id
                            .action_helpViewPagerFragment_to_fragmentMainScreent
                    )
            )
    }

    fun changeColor(color: String) {
        when (color) {
            "R" -> {
                base_linear.setBackgroundColor(getColour(android.R.color.holo_red_dark))
                page_one_helpscreen_cl.visibility = View.VISIBLE
            }
            "G" -> {
                base_linear.setBackgroundColor(getColour(android.R.color.holo_green_light))
                page_two_helpscreen_cl.visibility = View.VISIBLE
            }
            "B" -> {
                base_linear.setBackgroundColor(getColour(android.R.color.holo_blue_dark))
                page_tree_helpscreen_cl.visibility = View.VISIBLE
                skip_text_view.text = getString(R.string.done)
            }
        }
    }

    private fun getColour(id: Int): Int {
        var currentColor = 0
        context?.let {
            currentColor = ContextCompat.getColor(it, id)
        }
        return currentColor
    }

    private companion object {
        const val DELAY_FOR_ANIMATION = 500L
        const val DELAY_BETWEEN_ANIMATIONS = 750L
    }
}

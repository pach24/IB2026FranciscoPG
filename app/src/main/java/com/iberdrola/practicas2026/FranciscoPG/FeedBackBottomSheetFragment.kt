package com.iberdrola.practicas2026.FranciscoPG

import android.content.DialogInterface
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.iberdrola.practicas2026.FranciscoPG.databinding.FragmentFeedbackBottomSheetBinding

class FeedbackBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentFeedbackBottomSheetBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "FeedbackBottomSheetFragment"

        fun newInstance(): FeedbackBottomSheetFragment {
            return FeedbackBottomSheetFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedbackBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        binding.tvLater.paintFlags = binding.tvLater.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        val dismissListener = View.OnClickListener { dismiss() }

        binding.ivFaceVerySad.setOnClickListener(dismissListener)
        binding.ivFaceSad.setOnClickListener(dismissListener)
        binding.ivFaceNeutral.setOnClickListener(dismissListener)
        binding.ivFaceHappy.setOnClickListener(dismissListener)
        binding.ivFaceVeryHappy.setOnClickListener(dismissListener)

        binding.tvLater.setOnClickListener(dismissListener)
    }

    // Finaliza la actividad anfitriona para regresar a MainActivity
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

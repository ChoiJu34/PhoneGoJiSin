package com.ssafy.phonesin.ui.mobile.returnmobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ssafy.phonesin.R
import com.ssafy.phonesin.databinding.FragmentReturnMobileBinding
import com.ssafy.phonesin.ui.MainActivity
import com.ssafy.phonesin.ui.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReturnMobileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ReturnMobileFragment : BaseFragment<FragmentReturnMobileBinding>(
    R.layout.fragment_return_mobile
) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val returnMobileViewModel : ReturnMobileViewModel by viewModels()

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReturnMobileBinding {
        return FragmentReturnMobileBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun init() {
        setReturnMobile()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavi(true)
    }


    private fun setReturnMobile() = with(bindingNonNull) {

        returnMobileViewModel.rentalResponseList.observe(viewLifecycleOwner){
            radioGroupReturnAdd.removeAllViews()
            it.forEach {
                val radioButton = RadioButton(context)
                radioButton.text = "${it.phoneId}"
                radioGroupReturnAdd.addView(radioButton)
            }
        }

        for (i in 1..3) {
            val radioButton = RadioButton(context)
            radioButton.text = "RadioButton $i"
            radioButton.id = i // 라디오 버튼마다 고유한 ID를 부여 (무조건 필요한 것은 아님)
            radioGroupReturnAdd.addView(radioButton)
        }

        buttonReturnNext.setOnClickListener {
            if (radioButtonAgent.isChecked) {
                findNavController().navigate(
                    R.id.action_returnMobileFragment_to_returnAgentFragment,
                )
            } else {
                findNavController().navigate(
                    R.id.action_returnMobileFragment_to_returnVisitDeliveryFragment,
                )
            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReturnMobileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReturnMobileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
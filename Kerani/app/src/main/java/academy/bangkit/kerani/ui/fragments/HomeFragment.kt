package academy.bangkit.kerani.ui.fragments

import academy.bangkit.kerani.R
import academy.bangkit.kerani.databinding.FragmentHomeBinding
import academy.bangkit.kerani.ui.activities.CameraActivity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        showLoading(false)
        val dropdownItem = resources.getStringArray(R.array.plants)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, dropdownItem)
        binding.dropdown.setAdapter(arrayAdapter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDiagnose.setOnClickListener {
            showLoading(true)
            val intent = Intent(context, CameraActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.opentry.android_storage_samples.safdemos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.opentry.android_storage_samples.R
import com.opentry.android_storage_samples.databinding.FragmentMainSafDemosBinding
import com.opentry.android_storage_samples.databinding.ListItemDemoBinding

class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainSafDemosBinding.inflate(layoutInflater)

        val demoItems = arrayOf(
            // TODO: Add other SAF demos
            Demo(
                getString(R.string.image_picker_demo_title),
                getString(R.string.image_picker_demo_text),
                R.drawable.ic_image_black_24dp,
                R.id.action_mainFragment_to_imagePickerFragment
            )
        )

        val adapter = DemoAdapter(demoItems) { clickedDemo ->
            findNavController().navigate(clickedDemo.action)
        }
        binding.demosList.adapter = adapter

        return binding.root
    }
}

data class Demo(
    val title: String,
    val text: String,
    @DrawableRes val icon: Int,
    @IdRes val action: Int
)

private class DemoViewHolder(val binding: ListItemDemoBinding) :
    RecyclerView.ViewHolder(binding.root)

private class DemoAdapter(
    private val demos: Array<Demo>,
    private val itemClickListener: (Demo) -> Unit
) : RecyclerView.Adapter<DemoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context).let { layoutInflater ->
            DemoViewHolder(ListItemDemoBinding.inflate(layoutInflater, parent, false))
        }

    override fun getItemCount() = demos.size

    override fun onBindViewHolder(holder: DemoViewHolder, position: Int) {
        val demo = demos[position]
        holder.binding.demoIcon.setImageResource(demo.icon)
        holder.binding.demoTitle.text = demo.title
        holder.binding.demoText.text = demo.text
        holder.binding.root.setOnClickListener { itemClickListener(demos[position]) }
    }
}
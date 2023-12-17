import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.shouldiski.R

class LookoutDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_lookout_dialog, container, false)

        val titleTextView: TextView = view.findViewById(R.id.dialogTitle)
        val mainTextView: TextView = view.findViewById(R.id.dialogMainText)

        // Retrieve the arguments
        val title = arguments?.getString("title") ?: "No Data"
        val mainText = arguments?.getString("mainText") ?: "No Data"

        titleTextView.text = title
        mainTextView.text = mainText

        return view
    }


    override fun onStart() {
        super.onStart()
        dialog?.let {
            it.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            it.setCanceledOnTouchOutside(true) // close dialog when clicked outside dialog
        }
    }
}
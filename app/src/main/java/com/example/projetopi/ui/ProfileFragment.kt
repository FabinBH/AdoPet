import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projetopi.R
import com.example.projetopi.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        carregarDados()
        configurarLogout()
    }

    private fun carregarDados() {
        val uid = auth.currentUser?.uid ?: return

        val userRef = db.child("usuarios").child(uid).child("cadastro")

        userRef.get().addOnSuccessListener { snapshot ->
            val pf = snapshot.child("PessoaFisica")
            val pj = snapshot.child("PessoaJuridica")

            if (pf.exists()) {
                val nome = pf.child("nome").value.toString()

                binding.textViewNome.text = nome
                binding.textViewEmail.text = auth.currentUser?.email
            } else if (pj.exists()) {
                val nome = pj.child("nome").value.toString()

                binding.textViewNome.text = nome
                binding.textViewEmail.text = auth.currentUser?.email
            }
        }
    }

    private fun configurarLogout() {
        binding.buttonLogout.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_profileFragment_to_authentication2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

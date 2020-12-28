package com.example.xhh.ui.game

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xhh.R
import com.example.xhh.ui.game.model.CardMatchingGame
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.lang.Exception

const val gameFile = "gameFile"
class GameFragment : Fragment() {

    val cardButtons = mutableListOf<Button>()
    private lateinit var dashboardViewModel: GameViewModel
    lateinit var adapter: CardRecyclerViewAdapter
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recylerView = view.findViewById<RecyclerView>(R.id.recylerView1)
        val reset = view.findViewById<Button>(R.id.reset)

        val rgame = loadData()
        if (rgame != null) {
            game = rgame
        }else{
            game = CardMatchingGame(24)
        }
        adapter = CardRecyclerViewAdapter(game)
        recylerView.adapter = adapter
        val configuration = resources.configuration
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recylerView.layoutManager = GridLayoutManager(activity, 6)
        }else{
            val gridLayoutManager = GridLayoutManager(activity, 4)
            recylerView.layoutManager = gridLayoutManager
        }
        updateUI()
        adapter.setOnCardClickListener {
            game.chooseCardAtIndex(it)
            updateUI()
        }

        reset.setOnClickListener {
            game.reset()
            updateUI()
        }
    }


    fun updateUI() {
        adapter.notifyDataSetChanged()
        val score = view?.findViewById<TextView>(R.id.score)
        adapter.notifyDataSetChanged()
        score?.text = String.format("%s%d",getString(R.string.score),game.score)
        score?.text = getString(R.string.score) + game.score
    }

    override fun onStop() {
        super.onStop()
        saveData()
    }

    fun saveData() {
        try {
            val output = activity?.openFileOutput(gameFile, AppCompatActivity.MODE_PRIVATE)
            val objectOutputStream = ObjectOutputStream(output)
            objectOutputStream.writeObject(game)
            objectOutputStream.close()
            output?.close()
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun loadData(): CardMatchingGame? {
        try {
            val input = activity?.openFileInput(gameFile)
            val objectInputStream =  ObjectInputStream(input)
            val game = objectInputStream.readObject() as CardMatchingGame
            objectInputStream.close()
            input?.close()
            return game
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    companion object {
        lateinit var game: CardMatchingGame
        fun newInstance() = GameFragment()
    }
}
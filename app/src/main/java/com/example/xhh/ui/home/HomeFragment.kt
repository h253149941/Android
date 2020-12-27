package com.example.xhh.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xhh.R
import com.example.xhh.ui.watch.WatchViewModel
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.lang.Exception
import java.lang.StringBuilder

const val MyMessage = "com.example.fragmentdemo2.message"
class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    lateinit var msgList: MutableList<Msg>
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val msgs = loadData()

        if (msgs == null)
        {
            msgList = mutableListOf()
        }else {
            msgList = msgs
        }

        val adapter = MessageAdapter(msgList)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val button_send = view.findViewById<Button>(R.id.button_send)
        recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        button_send.setOnClickListener {
            val editMessage = view.findViewById<EditText>(R.id.editMessage)
            val message = editMessage.text.toString()
            val msg = addMsg(message)
            msgList.add(msg)
            adapter.notifyItemInserted(msgList.size - 1)
            recyclerView.scrollToPosition(msgList.size - 1)

            val intent = Intent(MyMessage)
            intent.setPackage(activity?.packageName)
            activity?.sendBroadcast(intent)
        }
    }

    override fun onStop() {
        super.onStop()
        saveData()
    }

    fun loadData():MutableList<Msg>? {
        try {
            val input = activity?.openFileInput("data")
            val objectInputStream = ObjectInputStream(input)

            val msgs = objectInputStream.readObject() as MutableList<Msg>

            objectInputStream.close()
            input?.close()
            return msgs

        }catch (e: Exception){
            return null
        }


    }

    fun saveData() {
        val output = activity?.openFileOutput("data",Context.MODE_PRIVATE)
        val objectOutputStream = ObjectOutputStream(output)

        objectOutputStream.writeObject(msgList)

        objectOutputStream.close()
        output?.close()
    }

    fun addMsg(str: String): Msg{
        val count = (0..10).random()
        val builder = StringBuilder()
        for (i in 0..count){
            builder.append(str)
        }
        return Msg(builder.toString(),(0..1).random())
    }

    data class Msg(val content:String, val type:Int): Serializable {
        companion object {
            const val REICEIVE = 0
            const val SEND = 1
        }
    }

    class MessageAdapter(val list:List<Msg>): RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
        class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
            val leftMsg: TextView
            val rightMsg: TextView
            init {
                leftMsg = view.findViewById(R.id.textView_left)
                rightMsg = view.findViewById(R.id.textView_right)
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MessageAdapter.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: MessageAdapter.ViewHolder, position: Int) {
            val msg = list.get(position)
            if(msg.type == Msg.REICEIVE) {
                holder.rightMsg.visibility = View.GONE
                holder.leftMsg.visibility = View.VISIBLE
                holder.leftMsg.text = msg.content
            }else{
                holder.leftMsg.visibility = View.GONE
                holder.rightMsg.visibility = View.VISIBLE
                holder.rightMsg.text = msg.content
            }
        }

        override fun getItemCount(): Int {
            return list.size
        }


    }
}
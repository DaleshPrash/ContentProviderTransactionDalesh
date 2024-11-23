package com.example.contentprovidercontactsdaleshpr
import android.content.ContentValues
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contentprovidercontactsdalesh.databinding.ActivityContentProviderBinding


class ContentProviderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContentProviderBinding
    private val transactions = mutableListOf<Transaction>()
    private val adapter = TransactionAdapter(transactions)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentProviderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.transactionRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.transactionRecyclerView.adapter = adapter

        binding.insertButton.setOnClickListener {
            val description = binding.descriptionEditText.text.toString()
            val amount = binding.amountEditText.text.toString().toDoubleOrNull()
            if (description.isNotBlank() && amount != null) {
                val values = ContentValues().apply {
                    put(CustomDatabaseHelper.COLUMN_DESCRIPTION, description)
                    put(CustomDatabaseHelper.COLUMN_AMOUNT, amount)
                }
                contentResolver.insert(TransactionContentProvider.CONTENT_URI, values)
                Toast.makeText(this, "Data inserted", Toast.LENGTH_SHORT).show()
                binding.descriptionEditText.text.clear()
                binding.amountEditText.text.clear()
            } else {
                Toast.makeText(this, "Please enter valid data", Toast.LENGTH_SHORT).show()
            }
        }

        binding.queryButton.setOnClickListener {
            queryDataFromProvider()
        }
    }

    private fun queryDataFromProvider() {
        transactions.clear()
        val cursor = contentResolver.query(TransactionContentProvider.CONTENT_URI, null, null, null, null)
        cursor?.let {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(CustomDatabaseHelper.COLUMN_ID))
                val description = it.getString(it.getColumnIndexOrThrow(CustomDatabaseHelper.COLUMN_DESCRIPTION))
                val amount = it.getDouble(it.getColumnIndexOrThrow(CustomDatabaseHelper.COLUMN_AMOUNT))
                transactions.add(Transaction(id, description, amount))
            }
            cursor.close()
        }
        adapter.notifyDataSetChanged()
    }
}
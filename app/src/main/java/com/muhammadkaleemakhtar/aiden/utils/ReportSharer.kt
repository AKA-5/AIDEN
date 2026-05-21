package com.muhammadkaleemakhtar.aiden.utils

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.muhammadkaleemakhtar.aiden.agents.AgentLogger
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ReportSharer {
    fun shareReport(context: Context) {
        try {
            val logs = AgentLogger.exportToJson()
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val fileName = "AIDEN_logs_$timestamp.json"
            
            // Try public Downloads directory first
            var file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)
            
            try {
                file.parentFile?.mkdirs()
                file.writeText(logs)
            } catch (e: Exception) {
                // Fall back to sandbox downloads directory if permission is denied (API 29+)
                file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)
                file.writeText(logs)
            }
            
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/json"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Export Agent Logs"))
            Toast.makeText(context, "Exported successfully: ${file.name}", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Export failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

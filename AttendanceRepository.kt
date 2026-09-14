package com.example.ashishatte.data

import com.example.ashishatte.model.Employee
import com.example.ashishatte.model.OfficeLocation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AttendanceRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    suspend fun currentEmployee(): Employee? {
        val uid = auth.currentUser?.uid ?: return null
        return db.collection("employees").document(uid).get().await()
            .toObject(Employee::class.java)
    }

    suspend fun office(): OfficeLocation? =
        db.collection("settings").document("office").get().await()
            .toObject(OfficeLocation::class.java)

    suspend fun saveAttendance(
        employee: Employee,
        type: String,
        latitude: Double,
        longitude: Double,
        distanceMeters: Double,
        evidencePath: String? = null
    ) {
        require(type == "CHECK_IN" || type == "CHECK_OUT") { "Invalid attendance type." }
        require(distanceMeters >= 0) { "Invalid distance." }

        val ref = db.collection("attendance").document()
        val record = hashMapOf(
            "id" to ref.id,
            "employeeUid" to employee.uid,
            "employeeName" to employee.name,
            "type" to type,
            "clientCreatedAtMillis" to System.currentTimeMillis(),
            "serverCreatedAt" to FieldValue.serverTimestamp(),
            "latitude" to latitude,
            "longitude" to longitude,
            "distanceMeters" to distanceMeters,
            "selfieEvidencePath" to evidencePath
        )
        ref.set(record).await()
    }

    suspend fun setOffice(location: OfficeLocation) {
        require(location.latitude in -90.0..90.0) { "Invalid latitude." }
        require(location.longitude in -180.0..180.0) { "Invalid longitude." }
        require(location.radiusMeters in 10.0..5000.0) { "Radius must be 10–5000 meters." }
        db.collection("settings").document("office").set(location).await()
    }
}

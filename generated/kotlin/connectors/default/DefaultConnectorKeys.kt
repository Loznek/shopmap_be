
@file:Suppress(
  "KotlinRedundantDiagnosticSuppress",
  "LocalVariableName",
  "MayBeConstant",
  "RedundantVisibilityModifier",
  "RemoveEmptyClassBody",
  "SpellCheckingInspection",
  "LocalVariableName",
  "unused",
)

@file:UseSerializers(DateSerializer::class, UUIDSerializer::class, TimestampSerializer::class)

package connectors.default

import kotlinx.serialization.Serializable

import kotlinx.serialization.UseSerializers
import com.google.firebase.dataconnect.serializers.DateSerializer
import com.google.firebase.dataconnect.serializers.UUIDSerializer
import com.google.firebase.dataconnect.serializers.TimestampSerializer


  @Serializable
  public data class DepartmentKey(
  
    val id:
    java.util.UUID
  ) {
    
    
  }

  @Serializable
  public data class MapKey(
  
    val id:
    java.util.UUID
  ) {
    
    
  }

  @Serializable
  public data class ProductKey(
  
    val id:
    java.util.UUID
  ) {
    
    
  }

  @Serializable
  public data class ShelfKey(
  
    val id:
    java.util.UUID
  ) {
    
    
  }

  @Serializable
  public data class StoreKey(
  
    val id:
    java.util.UUID
  ) {
    
    
  }

  @Serializable
  public data class TillKey(
  
    val id:
    java.util.UUID
  ) {
    
    
  }

  @Serializable
  public data class WallBlockKey(
  
    val id:
    java.util.UUID
  ) {
    
    
  }


// The lines below are used by the code generator to ensure that this file is deleted if it is no
// longer needed. Any files in this directory that contain the lines below will be deleted by the
// code generator if the file is no longer needed. If, for some reason, you do _not_ want the code
// generator to delete this file, then remove the line below (and this comment too, if you want).

// FIREBASE_DATA_CONNECT_GENERATED_FILE MARKER 42da5e14-69b3-401b-a9f1-e407bee89a78
// FIREBASE_DATA_CONNECT_GENERATED_FILE CONNECTOR default

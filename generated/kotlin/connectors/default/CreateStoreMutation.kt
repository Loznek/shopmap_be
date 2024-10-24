
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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.serializer

import com.google.firebase.dataconnect.MutationRef
import com.google.firebase.dataconnect.MutationResult

import com.google.firebase.dataconnect.OptionalVariable
import com.google.firebase.dataconnect.generated.GeneratedMutation

import kotlinx.serialization.UseSerializers
import com.google.firebase.dataconnect.serializers.DateSerializer
import com.google.firebase.dataconnect.serializers.UUIDSerializer
import com.google.firebase.dataconnect.serializers.TimestampSerializer

public interface CreateStoreMutation :
    GeneratedMutation<
      DefaultConnector,
      CreateStoreMutation.Data,
      CreateStoreMutation.Variables
    >
{
  
    @Serializable
  public data class Variables(
  
    val name:
    String,
    val location:
    String
  ) {
    
    
  }
  

  
    @Serializable
  public data class Data(
  @SerialName("store_insert")
    val key:
    StoreKey
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "CreateStore"
    public val dataDeserializer: DeserializationStrategy<Data> = serializer()
    public val variablesSerializer: SerializationStrategy<Variables> = serializer()
  }
}

public fun CreateStoreMutation.ref(
  
    name: String,location: String,
  
  
): MutationRef<
    CreateStoreMutation.Data,
    CreateStoreMutation.Variables
  > =
  ref(
    
      CreateStoreMutation.Variables(
        name=name,location=location,
  
      )
    
  )

public suspend fun CreateStoreMutation.execute(
  
    name: String,location: String,
  
  
  ): MutationResult<
    CreateStoreMutation.Data,
    CreateStoreMutation.Variables
  > =
  ref(
    
      name=name,location=location,
  
    
  ).execute()



// The lines below are used by the code generator to ensure that this file is deleted if it is no
// longer needed. Any files in this directory that contain the lines below will be deleted by the
// code generator if the file is no longer needed. If, for some reason, you do _not_ want the code
// generator to delete this file, then remove the line below (and this comment too, if you want).

// FIREBASE_DATA_CONNECT_GENERATED_FILE MARKER 42da5e14-69b3-401b-a9f1-e407bee89a78
// FIREBASE_DATA_CONNECT_GENERATED_FILE CONNECTOR default

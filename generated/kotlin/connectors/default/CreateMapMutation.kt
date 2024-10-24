
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

public interface CreateMapMutation :
    GeneratedMutation<
      DefaultConnector,
      CreateMapMutation.Data,
      CreateMapMutation.Variables
    >
{
  
    @Serializable
  public data class Variables(
  
    val store:
    java.util.UUID,
    val width:
    Int,
    val height:
    Int,
    val entranceX:
    Int,
    val entranceY:
    Int,
    val exitX:
    Int,
    val exitY:
    Int
  ) {
    
    
  }
  

  
    @Serializable
  public data class Data(
  @SerialName("map_insert")
    val key:
    MapKey
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "CreateMap"
    public val dataDeserializer: DeserializationStrategy<Data> = serializer()
    public val variablesSerializer: SerializationStrategy<Variables> = serializer()
  }
}

public fun CreateMapMutation.ref(
  
    store: java.util.UUID,width: Int,height: Int,entranceX: Int,entranceY: Int,exitX: Int,exitY: Int,
  
  
): MutationRef<
    CreateMapMutation.Data,
    CreateMapMutation.Variables
  > =
  ref(
    
      CreateMapMutation.Variables(
        store=store,width=width,height=height,entranceX=entranceX,entranceY=entranceY,exitX=exitX,exitY=exitY,
  
      )
    
  )

public suspend fun CreateMapMutation.execute(
  
    store: java.util.UUID,width: Int,height: Int,entranceX: Int,entranceY: Int,exitX: Int,exitY: Int,
  
  
  ): MutationResult<
    CreateMapMutation.Data,
    CreateMapMutation.Variables
  > =
  ref(
    
      store=store,width=width,height=height,entranceX=entranceX,entranceY=entranceY,exitX=exitX,exitY=exitY,
  
    
  ).execute()



// The lines below are used by the code generator to ensure that this file is deleted if it is no
// longer needed. Any files in this directory that contain the lines below will be deleted by the
// code generator if the file is no longer needed. If, for some reason, you do _not_ want the code
// generator to delete this file, then remove the line below (and this comment too, if you want).

// FIREBASE_DATA_CONNECT_GENERATED_FILE MARKER 42da5e14-69b3-401b-a9f1-e407bee89a78
// FIREBASE_DATA_CONNECT_GENERATED_FILE CONNECTOR default

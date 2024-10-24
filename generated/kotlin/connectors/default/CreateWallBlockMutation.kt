
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

public interface CreateWallBlockMutation :
    GeneratedMutation<
      DefaultConnector,
      CreateWallBlockMutation.Data,
      CreateWallBlockMutation.Variables
    >
{
  
    @Serializable
  public data class Variables(
  
    val map:
    java.util.UUID,
    val width:
    Int,
    val height:
    Int,
    val startX:
    Int,
    val startY:
    Int
  ) {
    
    
  }
  

  
    @Serializable
  public data class Data(
  @SerialName("wall_block_insert")
    val key:
    WallBlockKey
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "CreateWallBlock"
    public val dataDeserializer: DeserializationStrategy<Data> = serializer()
    public val variablesSerializer: SerializationStrategy<Variables> = serializer()
  }
}

public fun CreateWallBlockMutation.ref(
  
    map: java.util.UUID,width: Int,height: Int,startX: Int,startY: Int,
  
  
): MutationRef<
    CreateWallBlockMutation.Data,
    CreateWallBlockMutation.Variables
  > =
  ref(
    
      CreateWallBlockMutation.Variables(
        map=map,width=width,height=height,startX=startX,startY=startY,
  
      )
    
  )

public suspend fun CreateWallBlockMutation.execute(
  
    map: java.util.UUID,width: Int,height: Int,startX: Int,startY: Int,
  
  
  ): MutationResult<
    CreateWallBlockMutation.Data,
    CreateWallBlockMutation.Variables
  > =
  ref(
    
      map=map,width=width,height=height,startX=startX,startY=startY,
  
    
  ).execute()



// The lines below are used by the code generator to ensure that this file is deleted if it is no
// longer needed. Any files in this directory that contain the lines below will be deleted by the
// code generator if the file is no longer needed. If, for some reason, you do _not_ want the code
// generator to delete this file, then remove the line below (and this comment too, if you want).

// FIREBASE_DATA_CONNECT_GENERATED_FILE MARKER 42da5e14-69b3-401b-a9f1-e407bee89a78
// FIREBASE_DATA_CONNECT_GENERATED_FILE CONNECTOR default

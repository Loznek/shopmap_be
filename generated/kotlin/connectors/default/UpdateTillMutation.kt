
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

public interface UpdateTillMutation :
    GeneratedMutation<
      DefaultConnector,
      UpdateTillMutation.Data,
      UpdateTillMutation.Variables
    >
{
  
    @Serializable
  public data class Variables(
  
    val id:
    java.util.UUID,
    val map:
    OptionalVariable<java.util.UUID?>,
    val width:
    OptionalVariable<Int?>,
    val height:
    OptionalVariable<Int?>,
    val startX:
    OptionalVariable<Int?>,
    val startY:
    OptionalVariable<Int?>
  ) {
    
    
      
      @DslMarker public annotation class BuilderDsl

      @BuilderDsl
      public interface Builder {
        public var id: java.util.UUID
        public var map: java.util.UUID?
        public var width: Int?
        public var height: Int?
        public var startX: Int?
        public var startY: Int?
        
      }

      public companion object {
        @Suppress("NAME_SHADOWING")
        public fun build(
          id: java.util.UUID,
          block_: Builder.() -> Unit
        ): Variables {
          var id= id
            var map: OptionalVariable<java.util.UUID?> = OptionalVariable.Undefined
            var width: OptionalVariable<Int?> = OptionalVariable.Undefined
            var height: OptionalVariable<Int?> = OptionalVariable.Undefined
            var startX: OptionalVariable<Int?> = OptionalVariable.Undefined
            var startY: OptionalVariable<Int?> = OptionalVariable.Undefined
            

          return object : Builder {
            override var id: java.util.UUID
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { id = value_ }
              
            override var map: java.util.UUID?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { map = OptionalVariable.Value(value_) }
              
            override var width: Int?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { width = OptionalVariable.Value(value_) }
              
            override var height: Int?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { height = OptionalVariable.Value(value_) }
              
            override var startX: Int?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { startX = OptionalVariable.Value(value_) }
              
            override var startY: Int?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { startY = OptionalVariable.Value(value_) }
              
            
          }.apply(block_)
          .let {
            Variables(
              id=id,map=map,width=width,height=height,startX=startX,startY=startY,
            )
          }
        }
      }
    
  }
  

  
    @Serializable
  public data class Data(
  @SerialName("till_update")
    val key:
    TillKey?
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "UpdateTill"
    public val dataDeserializer: DeserializationStrategy<Data> = serializer()
    public val variablesSerializer: SerializationStrategy<Variables> = serializer()
  }
}

public fun UpdateTillMutation.ref(
  
    id: java.util.UUID,
  
    block_: UpdateTillMutation.Variables.Builder.() -> Unit
  
): MutationRef<
    UpdateTillMutation.Data,
    UpdateTillMutation.Variables
  > =
  ref(
    
      UpdateTillMutation.Variables.build(
        id=id,
  
    block_
      )
    
  )

public suspend fun UpdateTillMutation.execute(
  
    id: java.util.UUID,
  
    block_: UpdateTillMutation.Variables.Builder.() -> Unit
  
  ): MutationResult<
    UpdateTillMutation.Data,
    UpdateTillMutation.Variables
  > =
  ref(
    
      id=id,
  
    block_
    
  ).execute()



// The lines below are used by the code generator to ensure that this file is deleted if it is no
// longer needed. Any files in this directory that contain the lines below will be deleted by the
// code generator if the file is no longer needed. If, for some reason, you do _not_ want the code
// generator to delete this file, then remove the line below (and this comment too, if you want).

// FIREBASE_DATA_CONNECT_GENERATED_FILE MARKER 42da5e14-69b3-401b-a9f1-e407bee89a78
// FIREBASE_DATA_CONNECT_GENERATED_FILE CONNECTOR default

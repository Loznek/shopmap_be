
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

public interface UpdateMapMutation :
    GeneratedMutation<
      DefaultConnector,
      UpdateMapMutation.Data,
      UpdateMapMutation.Variables
    >
{
  
    @Serializable
  public data class Variables(
  
    val id:
    java.util.UUID,
    val store:
    OptionalVariable<java.util.UUID?>,
    val width:
    OptionalVariable<Int?>,
    val height:
    OptionalVariable<Int?>,
    val entranceX:
    OptionalVariable<Int?>,
    val entranceY:
    OptionalVariable<Int?>,
    val exitX:
    OptionalVariable<Int?>,
    val exitY:
    OptionalVariable<Int?>
  ) {
    
    
      
      @DslMarker public annotation class BuilderDsl

      @BuilderDsl
      public interface Builder {
        public var id: java.util.UUID
        public var store: java.util.UUID?
        public var width: Int?
        public var height: Int?
        public var entranceX: Int?
        public var entranceY: Int?
        public var exitX: Int?
        public var exitY: Int?
        
      }

      public companion object {
        @Suppress("NAME_SHADOWING")
        public fun build(
          id: java.util.UUID,
          block_: Builder.() -> Unit
        ): Variables {
          var id= id
            var store: OptionalVariable<java.util.UUID?> = OptionalVariable.Undefined
            var width: OptionalVariable<Int?> = OptionalVariable.Undefined
            var height: OptionalVariable<Int?> = OptionalVariable.Undefined
            var entranceX: OptionalVariable<Int?> = OptionalVariable.Undefined
            var entranceY: OptionalVariable<Int?> = OptionalVariable.Undefined
            var exitX: OptionalVariable<Int?> = OptionalVariable.Undefined
            var exitY: OptionalVariable<Int?> = OptionalVariable.Undefined
            

          return object : Builder {
            override var id: java.util.UUID
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { id = value_ }
              
            override var store: java.util.UUID?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { store = OptionalVariable.Value(value_) }
              
            override var width: Int?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { width = OptionalVariable.Value(value_) }
              
            override var height: Int?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { height = OptionalVariable.Value(value_) }
              
            override var entranceX: Int?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { entranceX = OptionalVariable.Value(value_) }
              
            override var entranceY: Int?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { entranceY = OptionalVariable.Value(value_) }
              
            override var exitX: Int?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { exitX = OptionalVariable.Value(value_) }
              
            override var exitY: Int?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { exitY = OptionalVariable.Value(value_) }
              
            
          }.apply(block_)
          .let {
            Variables(
              id=id,store=store,width=width,height=height,entranceX=entranceX,entranceY=entranceY,exitX=exitX,exitY=exitY,
            )
          }
        }
      }
    
  }
  

  
    @Serializable
  public data class Data(
  @SerialName("map_update")
    val key:
    MapKey?
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "UpdateMap"
    public val dataDeserializer: DeserializationStrategy<Data> = serializer()
    public val variablesSerializer: SerializationStrategy<Variables> = serializer()
  }
}

public fun UpdateMapMutation.ref(
  
    id: java.util.UUID,
  
    block_: UpdateMapMutation.Variables.Builder.() -> Unit
  
): MutationRef<
    UpdateMapMutation.Data,
    UpdateMapMutation.Variables
  > =
  ref(
    
      UpdateMapMutation.Variables.build(
        id=id,
  
    block_
      )
    
  )

public suspend fun UpdateMapMutation.execute(
  
    id: java.util.UUID,
  
    block_: UpdateMapMutation.Variables.Builder.() -> Unit
  
  ): MutationResult<
    UpdateMapMutation.Data,
    UpdateMapMutation.Variables
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

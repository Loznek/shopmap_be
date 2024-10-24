
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

public interface UpdateDepartmentMutation :
    GeneratedMutation<
      DefaultConnector,
      UpdateDepartmentMutation.Data,
      UpdateDepartmentMutation.Variables
    >
{
  
    @Serializable
  public data class Variables(
  
    val id:
    java.util.UUID,
    val type:
    OptionalVariable<String?>,
    val name:
    OptionalVariable<String?>,
    val width:
    OptionalVariable<Int?>,
    val height:
    OptionalVariable<Int?>,
    val startX:
    OptionalVariable<Int?>,
    val startY:
    OptionalVariable<Int?>,
    val map:
    OptionalVariable<java.util.UUID?>
  ) {
    
    
      
      @DslMarker public annotation class BuilderDsl

      @BuilderDsl
      public interface Builder {
        public var id: java.util.UUID
        public var type: String?
        public var name: String?
        public var width: Int?
        public var height: Int?
        public var startX: Int?
        public var startY: Int?
        public var map: java.util.UUID?
        
      }

      public companion object {
        @Suppress("NAME_SHADOWING")
        public fun build(
          id: java.util.UUID,
          block_: Builder.() -> Unit
        ): Variables {
          var id= id
            var type: OptionalVariable<String?> = OptionalVariable.Undefined
            var name: OptionalVariable<String?> = OptionalVariable.Undefined
            var width: OptionalVariable<Int?> = OptionalVariable.Undefined
            var height: OptionalVariable<Int?> = OptionalVariable.Undefined
            var startX: OptionalVariable<Int?> = OptionalVariable.Undefined
            var startY: OptionalVariable<Int?> = OptionalVariable.Undefined
            var map: OptionalVariable<java.util.UUID?> = OptionalVariable.Undefined
            

          return object : Builder {
            override var id: java.util.UUID
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { id = value_ }
              
            override var type: String?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { type = OptionalVariable.Value(value_) }
              
            override var name: String?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { name = OptionalVariable.Value(value_) }
              
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
              
            override var map: java.util.UUID?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { map = OptionalVariable.Value(value_) }
              
            
          }.apply(block_)
          .let {
            Variables(
              id=id,type=type,name=name,width=width,height=height,startX=startX,startY=startY,map=map,
            )
          }
        }
      }
    
  }
  

  
    @Serializable
  public data class Data(
  @SerialName("department_update")
    val key:
    DepartmentKey?
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "UpdateDepartment"
    public val dataDeserializer: DeserializationStrategy<Data> = serializer()
    public val variablesSerializer: SerializationStrategy<Variables> = serializer()
  }
}

public fun UpdateDepartmentMutation.ref(
  
    id: java.util.UUID,
  
    block_: UpdateDepartmentMutation.Variables.Builder.() -> Unit
  
): MutationRef<
    UpdateDepartmentMutation.Data,
    UpdateDepartmentMutation.Variables
  > =
  ref(
    
      UpdateDepartmentMutation.Variables.build(
        id=id,
  
    block_
      )
    
  )

public suspend fun UpdateDepartmentMutation.execute(
  
    id: java.util.UUID,
  
    block_: UpdateDepartmentMutation.Variables.Builder.() -> Unit
  
  ): MutationResult<
    UpdateDepartmentMutation.Data,
    UpdateDepartmentMutation.Variables
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

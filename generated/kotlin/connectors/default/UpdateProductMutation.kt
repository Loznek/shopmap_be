
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

public interface UpdateProductMutation :
    GeneratedMutation<
      DefaultConnector,
      UpdateProductMutation.Data,
      UpdateProductMutation.Variables
    >
{
  
    @Serializable
  public data class Variables(
  
    val id:
    java.util.UUID,
    val name:
    OptionalVariable<String?>,
    val size:
    OptionalVariable<Int?>,
    val shelf:
    OptionalVariable<java.util.UUID?>,
    val price:
    OptionalVariable<Int?>
  ) {
    
    
      
      @DslMarker public annotation class BuilderDsl

      @BuilderDsl
      public interface Builder {
        public var id: java.util.UUID
        public var name: String?
        public var size: Int?
        public var shelf: java.util.UUID?
        public var price: Int?
        
      }

      public companion object {
        @Suppress("NAME_SHADOWING")
        public fun build(
          id: java.util.UUID,
          block_: Builder.() -> Unit
        ): Variables {
          var id= id
            var name: OptionalVariable<String?> = OptionalVariable.Undefined
            var size: OptionalVariable<Int?> = OptionalVariable.Undefined
            var shelf: OptionalVariable<java.util.UUID?> = OptionalVariable.Undefined
            var price: OptionalVariable<Int?> = OptionalVariable.Undefined
            

          return object : Builder {
            override var id: java.util.UUID
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { id = value_ }
              
            override var name: String?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { name = OptionalVariable.Value(value_) }
              
            override var size: Int?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { size = OptionalVariable.Value(value_) }
              
            override var shelf: java.util.UUID?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { shelf = OptionalVariable.Value(value_) }
              
            override var price: Int?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { price = OptionalVariable.Value(value_) }
              
            
          }.apply(block_)
          .let {
            Variables(
              id=id,name=name,size=size,shelf=shelf,price=price,
            )
          }
        }
      }
    
  }
  

  
    @Serializable
  public data class Data(
  @SerialName("product_update")
    val key:
    ProductKey?
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "UpdateProduct"
    public val dataDeserializer: DeserializationStrategy<Data> = serializer()
    public val variablesSerializer: SerializationStrategy<Variables> = serializer()
  }
}

public fun UpdateProductMutation.ref(
  
    id: java.util.UUID,
  
    block_: UpdateProductMutation.Variables.Builder.() -> Unit
  
): MutationRef<
    UpdateProductMutation.Data,
    UpdateProductMutation.Variables
  > =
  ref(
    
      UpdateProductMutation.Variables.build(
        id=id,
  
    block_
      )
    
  )

public suspend fun UpdateProductMutation.execute(
  
    id: java.util.UUID,
  
    block_: UpdateProductMutation.Variables.Builder.() -> Unit
  
  ): MutationResult<
    UpdateProductMutation.Data,
    UpdateProductMutation.Variables
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
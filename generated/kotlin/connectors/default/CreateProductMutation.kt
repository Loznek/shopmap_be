
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

public interface CreateProductMutation :
    GeneratedMutation<
      DefaultConnector,
      CreateProductMutation.Data,
      CreateProductMutation.Variables
    >
{
  
    @Serializable
  public data class Variables(
  
    val name:
    String,
    val size:
    Int,
    val shelf:
    java.util.UUID,
    val price:
    Int
  ) {
    
    
  }
  

  
    @Serializable
  public data class Data(
  @SerialName("product_insert")
    val key:
    ProductKey
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "CreateProduct"
    public val dataDeserializer: DeserializationStrategy<Data> = serializer()
    public val variablesSerializer: SerializationStrategy<Variables> = serializer()
  }
}

public fun CreateProductMutation.ref(
  
    name: String,size: Int,shelf: java.util.UUID,price: Int,
  
  
): MutationRef<
    CreateProductMutation.Data,
    CreateProductMutation.Variables
  > =
  ref(
    
      CreateProductMutation.Variables(
        name=name,size=size,shelf=shelf,price=price,
  
      )
    
  )

public suspend fun CreateProductMutation.execute(
  
    name: String,size: Int,shelf: java.util.UUID,price: Int,
  
  
  ): MutationResult<
    CreateProductMutation.Data,
    CreateProductMutation.Variables
  > =
  ref(
    
      name=name,size=size,shelf=shelf,price=price,
  
    
  ).execute()



// The lines below are used by the code generator to ensure that this file is deleted if it is no
// longer needed. Any files in this directory that contain the lines below will be deleted by the
// code generator if the file is no longer needed. If, for some reason, you do _not_ want the code
// generator to delete this file, then remove the line below (and this comment too, if you want).

// FIREBASE_DATA_CONNECT_GENERATED_FILE MARKER 42da5e14-69b3-401b-a9f1-e407bee89a78
// FIREBASE_DATA_CONNECT_GENERATED_FILE CONNECTOR default

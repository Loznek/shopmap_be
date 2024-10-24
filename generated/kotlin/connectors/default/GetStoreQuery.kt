
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

import com.google.firebase.dataconnect.QueryRef
import com.google.firebase.dataconnect.QueryResult

  import kotlinx.coroutines.flow.Flow
  import kotlinx.coroutines.flow.filter
  import kotlinx.coroutines.flow.map

import com.google.firebase.dataconnect.OptionalVariable
import com.google.firebase.dataconnect.generated.GeneratedQuery

import kotlinx.serialization.UseSerializers
import com.google.firebase.dataconnect.serializers.DateSerializer
import com.google.firebase.dataconnect.serializers.UUIDSerializer
import com.google.firebase.dataconnect.serializers.TimestampSerializer

public interface GetStoreQuery :
    GeneratedQuery<
      DefaultConnector,
      GetStoreQuery.Data,
      GetStoreQuery.Variables
    >
{
  
    @Serializable
  public data class Variables(
  
    val id:
    java.util.UUID
  ) {
    
    
  }
  

  
    @Serializable
  public data class Data(
  
    val store:
    Store?
  ) {
    
      
        @Serializable
  public data class Store(
  
    val id:
    java.util.UUID,
    val name:
    String,
    val location:
    String
  ) {
    
    
  }
      
    
    
  }
  

  public companion object {
    public val operationName: String = "getStore"
    public val dataDeserializer: DeserializationStrategy<Data> = serializer()
    public val variablesSerializer: SerializationStrategy<Variables> = serializer()
  }
}

public fun GetStoreQuery.ref(
  
    id: java.util.UUID,
  
  
): QueryRef<
    GetStoreQuery.Data,
    GetStoreQuery.Variables
  > =
  ref(
    
      GetStoreQuery.Variables(
        id=id,
  
      )
    
  )

public suspend fun GetStoreQuery.execute(
  
    id: java.util.UUID,
  
  
  ): QueryResult<
    GetStoreQuery.Data,
    GetStoreQuery.Variables
  > =
  ref(
    
      id=id,
  
    
  ).execute()


  public fun GetStoreQuery.flow(
    
      id: java.util.UUID,
  
    
    ): Flow<GetStoreQuery.Data> =
    ref(
        
          id=id,
  
        
      ).subscribe().flow.filter { it.result.isSuccess }.map { querySubscriptionResult ->
        querySubscriptionResult.result.getOrThrow().data
    }


// The lines below are used by the code generator to ensure that this file is deleted if it is no
// longer needed. Any files in this directory that contain the lines below will be deleted by the
// code generator if the file is no longer needed. If, for some reason, you do _not_ want the code
// generator to delete this file, then remove the line below (and this comment too, if you want).

// FIREBASE_DATA_CONNECT_GENERATED_FILE MARKER 42da5e14-69b3-401b-a9f1-e407bee89a78
// FIREBASE_DATA_CONNECT_GENERATED_FILE CONNECTOR default

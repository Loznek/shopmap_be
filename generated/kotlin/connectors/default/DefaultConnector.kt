
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

package connectors.default

import com.google.firebase.FirebaseApp
import com.google.firebase.dataconnect.ConnectorConfig
import com.google.firebase.dataconnect.DataConnectSettings
import com.google.firebase.dataconnect.FirebaseDataConnect
import com.google.firebase.dataconnect.generated.GeneratedConnector
import com.google.firebase.dataconnect.getInstance
import java.util.WeakHashMap

public interface DefaultConnector : GeneratedConnector {
  override val dataConnect: FirebaseDataConnect

  
    public val createDepartment: CreateDepartmentMutation
  
    public val createMap: CreateMapMutation
  
    public val createProduct: CreateProductMutation
  
    public val createShelf: CreateShelfMutation
  
    public val createStore: CreateStoreMutation
  
    public val createTill: CreateTillMutation
  
    public val createWallBlock: CreateWallBlockMutation
  
    public val deleteDepartment: DeleteDepartmentMutation
  
    public val deleteMap: DeleteMapMutation
  
    public val deleteProduct: DeleteProductMutation
  
    public val deleteShelf: DeleteShelfMutation
  
    public val deleteStore: DeleteStoreMutation
  
    public val deleteTill: DeleteTillMutation
  
    public val deleteWallBlock: DeleteWallBlockMutation
  
    public val getDepartmentsOnMap: GetDepartmentsOnMapQuery
  
    public val getMap: GetMapQuery
  
    public val getStore: GetStoreQuery
  
    public val getTillsOnMap: GetTillsOnMapQuery
  
    public val getWallBlocksOnMap: GetWallBlocksOnMapQuery
  
    public val updateDepartment: UpdateDepartmentMutation
  
    public val updateMap: UpdateMapMutation
  
    public val updateProduct: UpdateProductMutation
  
    public val updateShelf: UpdateShelfMutation
  
    public val updateStore: UpdateStoreMutation
  
    public val updateTill: UpdateTillMutation
  
    public val updateWallBlock: UpdateWallBlockMutation
  

  public companion object {
    @Suppress("MemberVisibilityCanBePrivate")
    public val config: ConnectorConfig = ConnectorConfig(
      connector = "default",
      location = "us-central1",
      serviceId = "shop_be",
    )

    public fun getInstance(
      dataConnect: FirebaseDataConnect
    ):DefaultConnector = synchronized(instances) {
      instances.getOrPut(dataConnect) {
        DefaultConnectorImpl(dataConnect)
      }
    }

    private val instances = WeakHashMap<FirebaseDataConnect, DefaultConnectorImpl>()
  }
}

public val DefaultConnector.Companion.instance:DefaultConnector
  get() = getInstance(FirebaseDataConnect.getInstance(config))

public fun DefaultConnector.Companion.getInstance(
  settings: DataConnectSettings = DataConnectSettings()
):DefaultConnector =
  getInstance(FirebaseDataConnect.getInstance(config, settings))

public fun DefaultConnector.Companion.getInstance(
  app: FirebaseApp,
  settings: DataConnectSettings = DataConnectSettings()
):DefaultConnector =
  getInstance(FirebaseDataConnect.getInstance(app, config, settings))

private class DefaultConnectorImpl(
  override val dataConnect: FirebaseDataConnect
) : DefaultConnector {
  
    override val createDepartment by lazy(LazyThreadSafetyMode.PUBLICATION) {
      CreateDepartmentMutationImpl(this)
    }
  
    override val createMap by lazy(LazyThreadSafetyMode.PUBLICATION) {
      CreateMapMutationImpl(this)
    }
  
    override val createProduct by lazy(LazyThreadSafetyMode.PUBLICATION) {
      CreateProductMutationImpl(this)
    }
  
    override val createShelf by lazy(LazyThreadSafetyMode.PUBLICATION) {
      CreateShelfMutationImpl(this)
    }
  
    override val createStore by lazy(LazyThreadSafetyMode.PUBLICATION) {
      CreateStoreMutationImpl(this)
    }
  
    override val createTill by lazy(LazyThreadSafetyMode.PUBLICATION) {
      CreateTillMutationImpl(this)
    }
  
    override val createWallBlock by lazy(LazyThreadSafetyMode.PUBLICATION) {
      CreateWallBlockMutationImpl(this)
    }
  
    override val deleteDepartment by lazy(LazyThreadSafetyMode.PUBLICATION) {
      DeleteDepartmentMutationImpl(this)
    }
  
    override val deleteMap by lazy(LazyThreadSafetyMode.PUBLICATION) {
      DeleteMapMutationImpl(this)
    }
  
    override val deleteProduct by lazy(LazyThreadSafetyMode.PUBLICATION) {
      DeleteProductMutationImpl(this)
    }
  
    override val deleteShelf by lazy(LazyThreadSafetyMode.PUBLICATION) {
      DeleteShelfMutationImpl(this)
    }
  
    override val deleteStore by lazy(LazyThreadSafetyMode.PUBLICATION) {
      DeleteStoreMutationImpl(this)
    }
  
    override val deleteTill by lazy(LazyThreadSafetyMode.PUBLICATION) {
      DeleteTillMutationImpl(this)
    }
  
    override val deleteWallBlock by lazy(LazyThreadSafetyMode.PUBLICATION) {
      DeleteWallBlockMutationImpl(this)
    }
  
    override val getDepartmentsOnMap by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetDepartmentsOnMapQueryImpl(this)
    }
  
    override val getMap by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetMapQueryImpl(this)
    }
  
    override val getStore by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetStoreQueryImpl(this)
    }
  
    override val getTillsOnMap by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetTillsOnMapQueryImpl(this)
    }
  
    override val getWallBlocksOnMap by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetWallBlocksOnMapQueryImpl(this)
    }
  
    override val updateDepartment by lazy(LazyThreadSafetyMode.PUBLICATION) {
      UpdateDepartmentMutationImpl(this)
    }
  
    override val updateMap by lazy(LazyThreadSafetyMode.PUBLICATION) {
      UpdateMapMutationImpl(this)
    }
  
    override val updateProduct by lazy(LazyThreadSafetyMode.PUBLICATION) {
      UpdateProductMutationImpl(this)
    }
  
    override val updateShelf by lazy(LazyThreadSafetyMode.PUBLICATION) {
      UpdateShelfMutationImpl(this)
    }
  
    override val updateStore by lazy(LazyThreadSafetyMode.PUBLICATION) {
      UpdateStoreMutationImpl(this)
    }
  
    override val updateTill by lazy(LazyThreadSafetyMode.PUBLICATION) {
      UpdateTillMutationImpl(this)
    }
  
    override val updateWallBlock by lazy(LazyThreadSafetyMode.PUBLICATION) {
      UpdateWallBlockMutationImpl(this)
    }
  

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "DefaultConnectorImpl(dataConnect=$dataConnect)"
}


  private class CreateDepartmentMutationImpl(
    override val connector: DefaultConnectorImpl
  ) : CreateDepartmentMutation {
  override val operationName by CreateDepartmentMutation.Companion::operationName
  override val dataDeserializer by CreateDepartmentMutation.Companion::dataDeserializer
  override val variablesSerializer by CreateDepartmentMutation.Companion::variablesSerializer

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "CreateDepartmentMutationImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

  private class CreateMapMutationImpl(
    override val connector: DefaultConnectorImpl
  ) : CreateMapMutation {
  override val operationName by CreateMapMutation.Companion::operationName
  override val dataDeserializer by CreateMapMutation.Companion::dataDeserializer
  override val variablesSerializer by CreateMapMutation.Companion::variablesSerializer

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "CreateMapMutationImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

  private class CreateProductMutationImpl(
    override val connector: DefaultConnectorImpl
  ) : CreateProductMutation {
  override val operationName by CreateProductMutation.Companion::operationName
  override val dataDeserializer by CreateProductMutation.Companion::dataDeserializer
  override val variablesSerializer by CreateProductMutation.Companion::variablesSerializer

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "CreateProductMutationImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

  private class CreateShelfMutationImpl(
    override val connector: DefaultConnectorImpl
  ) : CreateShelfMutation {
  override val operationName by CreateShelfMutation.Companion::operationName
  override val dataDeserializer by CreateShelfMutation.Companion::dataDeserializer
  override val variablesSerializer by CreateShelfMutation.Companion::variablesSerializer

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "CreateShelfMutationImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

  private class CreateStoreMutationImpl(
    override val connector: DefaultConnectorImpl
  ) : CreateStoreMutation {
  override val operationName by CreateStoreMutation.Companion::operationName
  override val dataDeserializer by CreateStoreMutation.Companion::dataDeserializer
  override val variablesSerializer by CreateStoreMutation.Companion::variablesSerializer

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "CreateStoreMutationImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

  private class CreateTillMutationImpl(
    override val connector: DefaultConnectorImpl
  ) : CreateTillMutation {
  override val operationName by CreateTillMutation.Companion::operationName
  override val dataDeserializer by CreateTillMutation.Companion::dataDeserializer
  override val variablesSerializer by CreateTillMutation.Companion::variablesSerializer

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "CreateTillMutationImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

  private class CreateWallBlockMutationImpl(
    override val connector: DefaultConnectorImpl
  ) : CreateWallBlockMutation {
  override val operationName by CreateWallBlockMutation.Companion::operationName
  override val dataDeserializer by CreateWallBlockMutation.Companion::dataDeserializer
  override val variablesSerializer by CreateWallBlockMutation.Companion::variablesSerializer

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "CreateWallBlockMutationImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

  private class DeleteDepartmentMutationImpl(
    override val connector: DefaultConnectorImpl
  ) : DeleteDepartmentMutation {
  override val operationName by DeleteDepartmentMutation.Companion::operationName
  override val dataDeserializer by DeleteDepartmentMutation.Companion::dataDeserializer
  override val variablesSerializer by DeleteDepartmentMutation.Companion::variablesSerializer

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "DeleteDepartmentMutationImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

  private class DeleteMapMutationImpl(
    override val connector: DefaultConnectorImpl
  ) : DeleteMapMutation {
  override val operationName by DeleteMapMutation.Companion::operationName
  override val dataDeserializer by DeleteMapMutation.Companion::dataDeserializer
  override val variablesSerializer by DeleteMapMutation.Companion::variablesSerializer

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "DeleteMapMutationImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

  private class DeleteProductMutationImpl(
    override val connector: DefaultConnectorImpl
  ) : DeleteProductMutation {
  override val operationName by DeleteProductMutation.Companion::operationName
  override val dataDeserializer by DeleteProductMutation.Companion::dataDeserializer
  override val variablesSerializer by DeleteProductMutation.Companion::variablesSerializer

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "DeleteProductMutationImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

  private class DeleteShelfMutationImpl(
    override val connector: DefaultConnectorImpl
  ) : DeleteShelfMutation {
  override val operationName by DeleteShelfMutation.Companion::operationName
  override val dataDeserializer by DeleteShelfMutation.Companion::dataDeserializer
  override val variablesSerializer by DeleteShelfMutation.Companion::variablesSerializer

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "DeleteShelfMutationImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

  private class DeleteStoreMutationImpl(
    override val connector: DefaultConnectorImpl
  ) : DeleteStoreMutation {
  override val operationName by DeleteStoreMutation.Companion::operationName
  override val dataDeserializer by DeleteStoreMutation.Companion::dataDeserializer
  override val variablesSerializer by DeleteStoreMutation.Companion::variablesSerializer

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "DeleteStoreMutationImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

  private class DeleteTillMutationImpl(
    override val connector: DefaultConnectorImpl
  ) : DeleteTillMutation {
  override val operationName by DeleteTillMutation.Companion::operationName
  override val dataDeserializer by DeleteTillMutation.Companion::dataDeserializer
  override val variablesSerializer by DeleteTillMutation.Companion::variablesSerializer

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "DeleteTillMutationImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

  private class DeleteWallBlockMutationImpl(
    override val connector: DefaultConnectorImpl
  ) : DeleteWallBlockMutation {
  override val operationName by DeleteWallBlockMutation.Companion::operationName
  override val dataDeserializer by DeleteWallBlockMutation.Companion::dataDeserializer
  override val variablesSerializer by DeleteWallBlockMutation.Companion::variablesSerializer

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "DeleteWallBlockMutationImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

  private class GetDepartmentsOnMapQueryImpl(
    override val connector: DefaultConnectorImpl
  ) : GetDepartmentsOnMapQuery {
  override val operationName by GetDepartmentsOnMapQuery.Companion::operationName
  override val dataDeserializer by GetDepartmentsOnMapQuery.Companion::dataDeserializer
  override val variablesSerializer by GetDepartmentsOnMapQuery.Companion::variablesSerializer

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "GetDepartmentsOnMapQueryImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

  private class GetMapQueryImpl(
    override val connector: DefaultConnectorImpl
  ) : GetMapQuery {
  override val operationName by GetMapQuery.Companion::operationName
  override val dataDeserializer by GetMapQuery.Companion::dataDeserializer
  override val variablesSerializer by GetMapQuery.Companion::variablesSerializer

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "GetMapQueryImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

  private class GetStoreQueryImpl(
    override val connector: DefaultConnectorImpl
  ) : GetStoreQuery {
  override val operationName by GetStoreQuery.Companion::operationName
  override val dataDeserializer by GetStoreQuery.Companion::dataDeserializer
  override val variablesSerializer by GetStoreQuery.Companion::variablesSerializer

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "GetStoreQueryImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

  private class GetTillsOnMapQueryImpl(
    override val connector: DefaultConnectorImpl
  ) : GetTillsOnMapQuery {
  override val operationName by GetTillsOnMapQuery.Companion::operationName
  override val dataDeserializer by GetTillsOnMapQuery.Companion::dataDeserializer
  override val variablesSerializer by GetTillsOnMapQuery.Companion::variablesSerializer

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "GetTillsOnMapQueryImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

  private class GetWallBlocksOnMapQueryImpl(
    override val connector: DefaultConnectorImpl
  ) : GetWallBlocksOnMapQuery {
  override val operationName by GetWallBlocksOnMapQuery.Companion::operationName
  override val dataDeserializer by GetWallBlocksOnMapQuery.Companion::dataDeserializer
  override val variablesSerializer by GetWallBlocksOnMapQuery.Companion::variablesSerializer

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "GetWallBlocksOnMapQueryImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

  private class UpdateDepartmentMutationImpl(
    override val connector: DefaultConnectorImpl
  ) : UpdateDepartmentMutation {
  override val operationName by UpdateDepartmentMutation.Companion::operationName
  override val dataDeserializer by UpdateDepartmentMutation.Companion::dataDeserializer
  override val variablesSerializer by UpdateDepartmentMutation.Companion::variablesSerializer

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "UpdateDepartmentMutationImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

  private class UpdateMapMutationImpl(
    override val connector: DefaultConnectorImpl
  ) : UpdateMapMutation {
  override val operationName by UpdateMapMutation.Companion::operationName
  override val dataDeserializer by UpdateMapMutation.Companion::dataDeserializer
  override val variablesSerializer by UpdateMapMutation.Companion::variablesSerializer

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "UpdateMapMutationImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

  private class UpdateProductMutationImpl(
    override val connector: DefaultConnectorImpl
  ) : UpdateProductMutation {
  override val operationName by UpdateProductMutation.Companion::operationName
  override val dataDeserializer by UpdateProductMutation.Companion::dataDeserializer
  override val variablesSerializer by UpdateProductMutation.Companion::variablesSerializer

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "UpdateProductMutationImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

  private class UpdateShelfMutationImpl(
    override val connector: DefaultConnectorImpl
  ) : UpdateShelfMutation {
  override val operationName by UpdateShelfMutation.Companion::operationName
  override val dataDeserializer by UpdateShelfMutation.Companion::dataDeserializer
  override val variablesSerializer by UpdateShelfMutation.Companion::variablesSerializer

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "UpdateShelfMutationImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

  private class UpdateStoreMutationImpl(
    override val connector: DefaultConnectorImpl
  ) : UpdateStoreMutation {
  override val operationName by UpdateStoreMutation.Companion::operationName
  override val dataDeserializer by UpdateStoreMutation.Companion::dataDeserializer
  override val variablesSerializer by UpdateStoreMutation.Companion::variablesSerializer

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "UpdateStoreMutationImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

  private class UpdateTillMutationImpl(
    override val connector: DefaultConnectorImpl
  ) : UpdateTillMutation {
  override val operationName by UpdateTillMutation.Companion::operationName
  override val dataDeserializer by UpdateTillMutation.Companion::dataDeserializer
  override val variablesSerializer by UpdateTillMutation.Companion::variablesSerializer

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "UpdateTillMutationImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

  private class UpdateWallBlockMutationImpl(
    override val connector: DefaultConnectorImpl
  ) : UpdateWallBlockMutation {
  override val operationName by UpdateWallBlockMutation.Companion::operationName
  override val dataDeserializer by UpdateWallBlockMutation.Companion::dataDeserializer
  override val variablesSerializer by UpdateWallBlockMutation.Companion::variablesSerializer

  override fun equals(other: Any?): Boolean = other === this

  override fun hashCode(): Int = System.identityHashCode(this)

  override fun toString() = "UpdateWallBlockMutationImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}


// The lines below are used by the code generator to ensure that this file is deleted if it is no
// longer needed. Any files in this directory that contain the lines below will be deleted by the
// code generator if the file is no longer needed. If, for some reason, you do _not_ want the code
// generator to delete this file, then remove the line below (and this comment too, if you want).

// FIREBASE_DATA_CONNECT_GENERATED_FILE MARKER 42da5e14-69b3-401b-a9f1-e407bee89a78
// FIREBASE_DATA_CONNECT_GENERATED_FILE CONNECTOR default

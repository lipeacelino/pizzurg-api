
# Pizzurg API

Este web service é uma API de delivery de uma pseudo hamburgueria e pizzaria, onde em tese essa API será consumida por um front end (site ou aplicativo).

Tem um funcionamento simples, onde: o dono do estabelecimento publica produtos (hambúrguer ou pizza), o cliente escolhe quais produtos quer, adiciona ao carrinho, faz o seu pedido e aguarda a entrega.


## Casos de Uso
### Diagrama de casos de uso

![Diagrama de casos de uso](https://drive.google.com/uc?id=1eTUekhEjF2zu9ebSW02rx37ZebCpwNEM)

### Caso de Uso 1: Fazer Login
Por padrão, a aplicação já deverá ter um administrador cadastrado para a pizzaria e hamburgueria. Os clientes deverão fazer login para poderem ver os produtos disponíveis, fazer pedidos e acompanhar o status dos pedidos.

### Caso de Uso 2: Criar Conta
Um cliente novo deverá criar uma conta para obter acesso através de um login. Ao criar a conta, deve ser informado: email e senha. O e-mail deve ser um identificador único de usuário. Sendo assim, só poderá ser criada uma conta com um e-mail que não esteja previamente cadastrado na API.

### Caso de Uso 3: Exclusão de Usuário (Cliente)
O administrador tem permissão para excluir um cliente. O cliente só poderá ser excluído se não tiver nenhum pedido associado a ele.

### Caso de Uso 4: Ver Produtos
Os clientes podem ver os produtos, assim como também podem fazer pesquisas por filtro (hambúrguer ou pizza) ou por nome do produto.

### Caso de Uso 5: Ver, Adicionar, Modificar Produtos e Excluir Produtos
Um administrador poderá adicionar, ver, atualizar, modificar e excluir um produto. No caso de exclusão, ela só poderá ser feita se o produto não estiver associado a nenhum pedido.

### Caso de Uso 6: Fazer Pedido
O pedido deverá conter informações do recebedor, endereço de entrega e forma de pagamento, e também todos os produtos comprados e suas respectivas quantidades.

### Caso de Uso 7: Acompanhar Status do Pedido
Após fazer o pedido, os clientes podem acompanhar o status do pedido.

### Caso de Uso 8: Ver Pedidos e Atualizar Status
Um administrador poderá ver os pedidos criados pelos clientes e modificar o status de acordo com o necessário (APPROVED, PREPARING, SENT, DELIVERED, CANCELED).

启动方法：
	命令行输入：java -jar BlockChainTest.jar 4
	4是要启动的节点个数，如果没有输入默认为4
默认的p2p监听端口从10000开始依次加1
默认的http监听端口从20000开始依次加1
API
获取节点1的账本数据：通过get请求http://127.0.0.1:20000/blocks
	返回数据格式为json：
[{
"data":"时间数据",
"hash":"区块hash",
"index":区块索引,
"pk":"打包区块的节点公钥",
"previousHash":"上一个区块的hash",
"signature":"对区块数据的签名",
"timestamp":时间戳,
"vN":viewNumber
}]
示例：[{"data":"1539758282107","hash":"17aa94136604dc7686708b02e861bc90987a4f840d0ac7ce7d823986af3616bc","index":2,"pk":"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCFXj+PCLGJ++8sAXEb8YrobMBnXrYGt4b9GT6t\r\nDuiR3n9nz0Agq2ep+cRhXPCwZPwZ7wEyRY5Vbl2Rb6ZunCD0Ht2IeAPi7EyTOwGSKfqbtWa5VLd+\r\nxtD6CmxarAMt/3TxGMp5GgOnetACvtpO5wZNNqYz31LR9WioFwlYVn/P2QIDAQAB","previousHash":"1db6aa3c81dc4b05a49eaed6feba99ed4ef07aa418d10bfbbc12af68cab6fb2a","signature":"ORdss911JdsUvfmUDpxUzLIW1zlbSKdywmSOkhQCADd0vQYXQnQlH/wViNNXgbe4kkNJOIK0cYc1\r\nY1xw76sUc6hb/KYjCsKncDa8zbRIv2Gpw3Kx8jVAbLHqXdAwyQWQWxV1iA2mEmjGf0lY9YccRZFo\r\ndJaOHeHWSySd/5hk7Ps=","timestamp":1539758282108,"vN":2}]


获取节点1的虚区块链数据：通过get请求http://127.0.0.1:20000/vblocks
	返回数据格式为json，且最多有8个块：
[{
"data":[{
"publicKey":"收到的ACK公钥",
"signature":"对ACK的签名",
"vN":viewNumber},{},{}......]
"hash":"区块hash",
"index":区块索引,
"pk":"打包区块的节点公钥",
"previousHash":"上一个区块的hash",
"timestamp":时间戳,
"viewNumber":viewNumber},
{},{}...
]
示例：
[{"data":"Hello VBlock","hash":"1db6aa3c81dc4b05a49eaed6feba99ed4ef07aa418d10bfbbc12af68cab6fb2a","index":1,"pk":"init pk","previousHash":"0","timestamp":0,"viewNumber":0},
{"data":"[{\"publicKey\":\"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCCIZ2rBKMYdtSL751cRZ0XLUVg3hDYiMTsvHmD\\r\\nyowuU6Y96qMU9v4pFTnu8W93XpyuFvBaogUsp9sI+RxSRB9vHSllfAQd1VzTc27By2mulxAEcxhL\\r\\nqtgsgwReLGQD+DXyU5FSUMQh+8yDAFugUTZcGampRwZ+MDsOVwXXlPMGTQIDAQAB\",\"sign\":\"XQ6LHUOj2hSXxxfjo2jfRYKy/ZvCcW4BN63Oc7XlgEhoRwX30agbyQzev2x6m5mGmAo2APkhkgBH\\r\\nURkulxFM+ODtx3Y8rd7cnIyS7FL41gr9aDWwxfwBUnj7dk1qI0M9mqqvtGsWHGfc5qkM9bRtxQpp\\r\\njmQEPt+X2ym6BeLM1NMumfLoWQP/UT/UJFflQ0DSMgTv8QU10NvmtkzeR1NnzW9BCniNCvuMtFj7\\r\\nCg6Crc7p7aydevyge1xTO8B/BAG8tuHBtoC7bU6s20iQQZSlZO6ne5BfX1pRQ1JKpl3jRAKcLZq5\\r\\n0+Zn8UsrpUq7vMZWy4khzXjZKPLwcEupnbv/bg==\",\"vN\":0},{\"publicKey\":\"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCCIZ2rBKMYdtSL751cRZ0XLUVg3hDYiMTsvHmD\\r\\nyowuU6Y96qMU9v4pFTnu8W93XpyuFvBaogUsp9sI+RxSRB9vHSllfAQd1VzTc27By2mulxAEcxhL\\r\\nqtgsgwReLGQD+DXyU5FSUMQh+8yDAFugUTZcGampRwZ+MDsOVwXXlPMGTQIDAQAB\",\"sign\":\"XQ6LHUOj2hSXxxfjo2jfRYKy/ZvCcW4BN63Oc7XlgEhoRwX30agbyQzev2x6m5mGmAo2APkhkgBH\\r\\nURkulxFM+ODtx3Y8rd7cnIyS7FL41gr9aDWwxfwBUnj7dk1qI0M9mqqvtGsWHGfc5qkM9bRtxQpp\\r\\njmQEPt+X2ym6BeLM1NMumfLoWQP/UT/UJFflQ0DSMgTv8QU10NvmtkzeR1NnzW9BCniNCvuMtFj7\\r\\nCg6Crc7p7aydevyge1xTO8B/BAG8tuHBtoC7bU6s20iQQZSlZO6ne5BfX1pRQ1JKpl3jRAKcLZq5\\r\\n0+Zn8UsrpUq7vMZWy4khzXjZKPLwcEupnbv/bg==\",\"vN\":0}]","hash":"3df8a39bd68b9de1aa9aa14a1792638de435944c6bfeb0e170242c8d1f282e56","index":2,"pk":"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCCIZ2rBKMYdtSL751cRZ0XLUVg3hDYiMTsvHmD\r\nyowuU6Y96qMU9v4pFTnu8W93XpyuFvBaogUsp9sI+RxSRB9vHSllfAQd1VzTc27By2mulxAEcxhL\r\nqtgsgwReLGQD+DXyU5FSUMQh+8yDAFugUTZcGampRwZ+MDsOVwXXlPMGTQIDAQAB","previousHash":"1db6aa3c81dc4b05a49eaed6feba99ed4ef07aa418d10bfbbc12af68cab6fb2a","timestamp":1539759220052,"viewNumber":1}]

获取节点1的正在连接的节点p2p端口列表：通过get请求http://127.0.0.1:20000/peers
	返回数据格式为json：
	[20001,20003,20002]

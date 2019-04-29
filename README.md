# excelrepots
Apache POIを使った検証

Apache POIは低速、メモリを大量に食うらしい。本当にその通りなのか確認してみることにした。

■測定条件
* 1セル=10バイト（数値、英数字）
* 1行=30セル=300バイト
* 行数=5万、10万、30万行

結果は、result/測定結果.xlsxを参照。

■ソースコードについて

特に意味はありませんが、Spring Boot + Spring Batchで作っています。Apache POIの処理は「CreateSXSSFWorkbookReportJobTasklet」にあります。
時間測定は、Spring Batchで定義したジョブの前後で行っています。JobListenerクラスに書いています。

■実行方法

何とかしてfatjarを作ってください。gradleからboot.jarを叩けばできるはず。手抜きで確認していません。fatjarさえ作れば、build.gradleにメインクラス設定しているので以下で動きます。（今気づいたけど、プロジェクト名をスペルミスしているな・・・）
<pre>
java -jar excelrepots.jar --spring.batch.job.enabled=true --spring.batch.job.names=createSXSSFWorkbookReportJob
</pre>

To encode: mvn exec:java -Dexec.mainClass="com.project.compression.HuffmanCompression" -Dexec.args="encode test0.txt"

To decode: mvn exec:java -Dexec.mainClass="com.project.compression.HuffmanCompression" -Dexec.args="decode"

Results might be inconsistent if your machine is not set to locale "en_US.UTF-8" and the original file contains non-ASCII
characters. 

For Mac users, run "locale" and make sure LANG and LC_ALL is set to "en_US.UTF-8". If not, "export" them. 

For a file size of 3.2 MB, a reduction in size to 2.1 MB is seen. 

![Screen Shot 2024-01-30 at 21 41 39 PM](https://github.com/arpande97/CompressionTool/assets/62608663/112f7ae5-c830-4da5-a2d2-0c4c59a15169)

The decoded file is same as the original file:

![Screen Shot 2024-01-30 at 21 42 18 PM](https://github.com/arpande97/CompressionTool/assets/62608663/059f9517-92eb-4b4e-b338-2d972c5db5cc)

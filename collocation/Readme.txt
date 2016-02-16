eg: He plays an important role in school.


一：第一遍筛选：从all.txt中找出15个与play role相似度较高的动名词搭配出来，以用于第二遍筛选。
all.txt: 包含了所有从中国英语学习者语料库 （CLEC）中正确的动名词搭配
将动词和名词都拆分开，计算动词之间与名词之间的相似度，并选出得分最高的15个动名词搭配。
基于WordNet英语词语相似度计算ws4j，找出两个动词之间共同的祖先，得到祖先到最近的结点的距离r1，同样也适用于名词，得到距离为r2。
分别计算两个动词在整个英语词库的使用频率P1，P2，以及两个词共同的祖先的词的频率P0，则两个词的距离为R = 2*P0 - (P1+P2)
则两个词的相似度即为1/R。


二：第二遍筛选：利用特征提取
提取一个句子的特征：Labour sought to play a role in achieving those goals .
其特征为：0 UnivL_in BivL_in_role UnivR_those BivR_those_goal UninL_those BinL_those_achieve BivI_in_those
UnivL：动词左边的第一个单词，BivL:动词左边的两个单词，UnivR:动词右边的第一个单词，BivI：动词中间的两个单词。
data文件下的文件为训练集，通过<key,value>的值保存到obj文件中，key即为一类为UnivL_to的值并以hash值保存，value即为to对相应的动
名词的影响值(double)，若遇到相同的key值需要不断的重复修改其value值，使其能适用于两个实例。这样从第一遍筛选过后的15个动名词搭配中对应
obj文件中的动名词搭配，将例句的特征提取出来与之计算得分，如果分较低或者得到的正负例为1则直接丢弃，这样筛选出一部分搭配。


三：第三遍筛选：利用N-gram语言模型进行筛选
根据test_1中的数据提取其相应的特征放到test_2中，使用N-gram三元组

这是对并发计算中使用的术语的非正式的顶层概述。 这样做的目的是为了就一个具有广泛相关性的主题提供一个非碎片化的资源，消除过载术语(overloaded terms)的歧义，并通过对某些术语置于比其传统意义更广泛的上下文语境中来帮助理解。目标读者是程序员和程序设计语言的学生。

*Work in progress; criticism welcome*

# 抽象层

抽象是指对问题形成简化的概念模型，使问题能够得到普遍或可重用的解决。例如，函数是一个抽象的概念，对解决代码的结构化和推理问题具有非常普遍的适用性(functions are an abstraction with very general applicability to solving the problem of structuring and reasoning about code)。具有高度通用性的抽象称为富抽象。

像计算这样的复杂领域中的问题是通过使用许多的抽象层来解决的。程序的执行大致可以分解为一个计算模型，用执行模型在机器模型中实现，其中机器模型在堆栈的底部。分层架构对于理解矛盾或看似排斥的概念可以在不同的抽象层共存，就像串行机器上的并发执行一样。当抽象与底层模型之间的矛盾暴露出来，而抽象出来的细节又不能忽视时，这就叫抽象中的漏洞。

处理漏洞的抽象通常来说是并发建模和编程的一个关键方面，所以本词汇表通过将抽象分组为两对术语来强调并发模型设计空间中可能存在的矛盾或分支路径。主题是按抽象程度的升序而不是按字母顺序排列的。

# Concurrent (order-independent) vs sequential

并发是指*独立*的计算可以以任意的顺序执行，并得到相同的结果。与并发相对应的是顺序，也就是说顺序计算要靠一步步执行才能产生正确的结果。

并发可以依靠计算之间的通信或交互来*限制*或约束，在保持正确性的前提下尽量减少依赖性是并发建模的核心问题。对通信的要求来自于模拟现实世界中并发进程之间的依赖关系，或者来自于提高效率和重复使用有限资源的需要。

并发编程是指将一个程序分解成独立的模块或并发单元。根据上下文，并发单元有不同的术语，如tasks、coroutine、process、threads或actors。

并发的广泛意义是基于将现实世界中的独立进程(independent processes)映射到计算模型(computational models)的需要，以及硬件上提高并行性的趋势和分布式计算和网络的兴起。

## Multi- prefix

有一组与并发相关的术语是以 "multiple"字开头的收缩词:multi-tasking, multi-programming, multi-processing, multi-threading 和 multi-core。multi-threading一般指的是使用系统线程，multi-core指的是硬件的并发，但其它术语都是模糊的，可以指一般的并发，也可以指更具体的东西，比如使用系统进程(system processes)或避免繁忙等待(avoiding busy-waiting)，所以最好避免使用模糊的术语。






package fr.warzou.skyblock.api.common.modification

sealed trait Type[A]
case class Add[A](_new: A) extends Type[A]
case class Remove[A](old: A) extends Type[A]
case class Set[A](old: A, _new: A) extends Type[A]
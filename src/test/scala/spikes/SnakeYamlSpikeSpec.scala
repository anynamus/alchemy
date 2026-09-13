package spikes

import org.scalatest.funsuite.AnyFunSuite
import org.snakeyaml.engine.v2.api.LoadSettings
import org.snakeyaml.engine.v2.api.lowlevel.Compose
import org.snakeyaml.engine.v2.nodes.{MappingNode, NodeType, SequenceNode}

class SnakeYamlSpikeSpec extends AnyFunSuite:

  test("SnakeYAML composes a mapping node"):

    val yaml =
      """
        |table: Customer
        |
        |columns:
        |  - name: id
        |    type: autonumber
        |""".stripMargin

    val settings = LoadSettings.builder()
      .setUseMarks(false) // Enables line/column mapping tracking
      .build()

    val compose = new Compose(settings)

    val maybeNode = compose.composeString(yaml)

    assert(maybeNode.isPresent)

    val node = maybeNode.get()

    node match
      case mappingNode: MappingNode => println(
          mappingNode.getValue.stream()
            .filter(v => v.getValueNode.getNodeType == NodeType.SEQUENCE)
            .map(v => v.getValueNode.asInstanceOf[SequenceNode].getValue)
            .toList
        )
      case _                        => fail("the main node is not a mapping...")

# Folder architecture extension 
* name/name.island -> main file, contain island structure
* name/nbt/*.dat -> tiles entities nbt file

# .island file format

## File structure

<table style="text-align:center">
<thead>
  <tr>
    <th>Name</th>
    <th colspan="3">Byte</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td rowspan="3">minecraft_version</td>
    <td rowspan="3">u3</td>
    <td style="font-style:italic">major</td>
    <td>u1</td>
  </tr>
  <tr>
    <td style="font-style:italic">minor</td>
    <td>u1</td>
  </tr>
  <tr>
    <td style="font-style:italic">revision</td>
    <td>u1</td>
  </tr>
  <tr>
    <td>block_entity_count</td>
    <td colspan="3">u2</td>
  </tr>
  <tr>
    <td>block_entities</td>
    <td colspan="3">nbt[nbt_count-1]</td>
  </tr>
  <tr>
    <td>used_blocks_count</td>
    <td colspan="3">u2</td>
  </tr>
  <tr>
    <td>used_blocks</td>
    <td colspan="3">block[used_blocks_count]</td>
  </tr>
  <tr>
    <td rowspan="3">cuboid_size</td>
    <td rowspan="3">u3</td>
    <td style="font-style:italic">width</td>
    <td>u1</td>
  </tr>
  <tr>
    <td style="font-style:italic">length</td>
    <td>u1</td>
  </tr>
  <tr>
    <td style="font-style:italic">height</td>
    <td>u1</td>
  </tr>
  <tr>
    <td>cuboid</td>
    <td colspan="3">varint&#185;[width*length*height]</td>
  </tr>
  <tr>
    <td>entities_count</td>
    <td colspan="3">u1</td>
  </tr>
  <tr>
    <td>entities</td>
    <td colspan="3">entity[entities_count]</td>
  </tr>
</tbody>
</table>

&#185; [Varint](https://wiki.vg/Protocol#VarInt_and_VarLong) protocol link

## String structure

<table style="text-align:center">
<thread>
  <tr>
    <th>Name</th>
    <th colspan="3">Byte</th>
  </tr>
</thread>
<tbody>
  <tr>
    <td>length</td>
    <td>u1</td>
  </tr>
  <tr>
    <td>chars</td>
    <td>byte[length]</td>
  </tr>
</tbody>
</table>

## Block structure

<table style="text-align:center">
<thead>
  <tr>
    <th>Name</th>
    <th colspan="3">Byte</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td>full_name<br></td>
    <td colspan="3">string<br></td>
  </tr>
  <tr>
    <td rowspan="3">data</td>
    <td colspan="3" rowspan="3">u1</td>
  </tr>
  <tr>
  </tr>
  <tr>
  </tr>
  <tr>
    <td>block_entity_index</td>
    <td>u2 (block_entity_count if no nbt)</td>
  </tr>
</tbody>
</table>

## Entity structure

<table style="text-align:center">
<thead>
  <tr>
    <th>Name</th>
    <th colspan="3">Byte</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td>location</td>
    <td>u8</td>
  </tr>
  <tr>
    <td>type_id</td>
    <td>u1</td>
  </tr>
  <tr>
    <td>nbt_tag</td>
    <td>nbt</td>
  </tr>
</tbody>
</table>

For location see protocol on [wiki.vg](https://wiki.vg/Protocol#Position)

## Nbt structure

<table style="text-align:center">
<thead>
  <td>Name</td>
  <td>Byte</td>
</thead>
<tbody>
  <tr>
    <td>length</td>
    <td>u2</td>
  </tr>
  <tr>
    <td>bytes</td>
    <td>byte[length]</td>
  </tr>
</tbody>
</table>
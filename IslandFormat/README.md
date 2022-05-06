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
    <td>signature</td>
    <td colspan="3">u4</td>
  </tr>
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
    <td>nbt_files_count</td>
    <td colspan="3">u2</td>
  </tr>
  <tr>
    <td>nbt_files</td>
    <td colspan="3">string[nbt_files_count]</td>
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
    <td rowspan="3">u6</td>
    <td style="font-style:italic">width</td>
    <td>u2</td>
  </tr>
  <tr>
    <td style="font-style:italic">length</td>
    <td>u2</td>
  </tr>
  <tr>
    <td style="font-style:italic">height</td>
    <td>u2</td>
  </tr>
  <tr>
    <td>cuboid</td>
    <td colspan="3">varint[width*length*height]</td>
  </tr>
  <tr>
    <td>used_entities_count</td>
    <td colspan="3">u1</td>
  </tr>
  <tr>
    <td>used_entities</td>
    <td colspan="3">used_entity[used_entities_count]</td>
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
    <td rowspan="3">states_count</td>
    <td colspan="3" rowspan="3">u2</td>
  </tr>
  <tr>
  </tr>
  <tr>
  </tr>
  <tr>
    <td>states</td>
    <td>state[states_count]</td>
  <tr>
    <td>nbt_files_index</td>
    <td>u2 (FFFF if no nbt)</td>
  </tr>
</tbody>
</table>

## Block state structure

<table style="text-align:center">
<thead>
  <tr>
    <th>Name</th>
    <th>Byte</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td>id</td>
    <td>u1</td>
  </tr>
  <tr>
    <td>value</td>
    <td>u1</td>
  </tr>
</tbody>
</table>

## Used entities structure

<table style="text-align:center">
<thead>
  <tr>
    <th>Name</th>
    <th>Byte</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td>nbt_file_index</td>
    <td>u2</td>
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
    <td>used_entities_index</td>
    <td colspan="3">u1</td>
  </tr>
  <tr>
    <td>location</td>
    <td>u8</td>
  </tr>
</tbody>
</table>

For location see protocol on [wiki.vg](https://wiki.vg/Protocol#Position)